/*
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database photesttogram;
*/
SELECT * FROM user;
DESC user;

SELECT * FROM user;
SELECT * FROM subscribe;
SELECT * FROM likes;
SELECT * FROM image;
SELECT * FROM comment;

SELECT * FROM image WHERE id IN (SELECT ImageId FROM likes WHERE userId = 1);

SELECT * FROM image WHERE id IN (SELECT C.ImageId FROM (SELECT ImageId, count(ImageId) likeCount FROM likes GROUP BY ImageId ORDER BY likeCount DESC) AS C);

SELECT C.ImageId FROM (SELECT ImageId, count(ImageId) likeCount FROM likes GROUP BY ImageId ORDER BY likeCount DESC) AS C;

SELECT i.*
FROM image i INNER JOIN (SELECT ImageId, count(ImageId) likeCount FROM likes GROUP BY ImageId) c
ON i.id = c.imageId
ORDER BY likeCount DESC;

SELECT i.* FROM image i INNER JOIN (SELECT ImageId, count(ImageId) likeCount FROM likes GROUP BY ImageId) c ON i.id = c.imageId ORDER BY likeCount DESC;


SELECT C.ImageId
FROM (
SELECT ImageId, count(ImageId) likeCount 
FROM likes GROUP BY ImageId ORDER BY likeCount DESC
) AS C;


-- 로그인한 사람이 구독하는 사람의 이미지 추출
SELECT * FROM image 
	WHERE userID IN (SELECT toUserId FROM subscribe WHERE fromUserId = 2);

-- 구독수
SELECT toUserId FROM subscribe WHERE fromUserId = 2;

-- 구독 여부 (ssar 로 로그인 1, cos page 로 감2)
SELECT COUNT(*) FROM subscribe WHERE fromUserId = 1 AND toUserId = 2;

-- 로그인 (1 ssar) -- 구독정보 (2 cos)

-- 1번과 3번의 정보가 (toUserId)가 구독 모달에 출력
SELECT * FROM subscribe;
SELECT * FROM user;

SELECT * FROM subscribe WHERE fromUserId = 2;
SELECT * FROM user WHERE id = 1 OR id = 3;

-- 조인 (user.id = subscribe.toUserId)
SELECT * FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

SELECT u.id, u.username, u.profileImageUrl FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;


-- 로그인 (1), 화면 (1, 3)
SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 1;
SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;

-- 가상 컬럼을 추가
SELECT u.id, u.username, u.profileImageUrl, 1 AS subscribeState
 FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

-- 스칼라 서브 쿼리 (단일행 리턴)
SELECT u.id, u.username, u.profileImageUrl, 
 (SELECT COUNT(*) FROM user) AS subscribeState
 FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;


-- 구독 여부 완성 쿼리
SELECT u.id, u.username, u.profileImageUrl, 
 (SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState
 FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;


SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 1;

-- 동일 유저인지 판단하는 쿼리 필요
SELECT u.id, u.username, u.profileImageUrl, 
 (SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) AS subscribeState,
 (1 = u.id) equalUserState
 FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;


-- 동일 유저인지 판단하는 쿼리 필요 (수정)
SELECT u.id, u.username, u.profileImageUrl, 
 if((SELECT 1 FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id), 1, 0) AS subscribeState,
 if((1 = u.id), 1, 0) equalUserState
 FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;


/*
SET FOREIGN_KEY_CHECKS = 0;  -- 제약 조건 잠시 끄기 0, 활성화 1
drop TABLE comment;
DROP TABLE image;
*/