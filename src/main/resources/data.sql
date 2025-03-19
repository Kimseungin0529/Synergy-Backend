---- 관심 분야
INSERT INTO interest (name, code) VALUES ('데이터 분석 / AI', 101);
INSERT INTO interest (name, code) VALUES ('클라우드 / DevOps', 102);
INSERT INTO interest (name, code) VALUES ('소프트웨어 개발', 103);
INSERT INTO interest (name, code) VALUES ('UX/UI 디자인', 104);
INSERT INTO interest (name, code) VALUES ('정보 보안', 105);
INSERT INTO interest (name, code) VALUES ('신기술 연구', 106);
INSERT INTO interest (name, code) VALUES ('커리어 개발', 107);
INSERT INTO interest (name, code) VALUES ('기획/운영', 108);

-- 직무 (Occupation Categories)
INSERT INTO occupation_category (code, name) VALUES (1, '개발');
INSERT INTO occupation_category (code, name) VALUES (2, '디자인');
INSERT INTO occupation_category (code, name) VALUES (3, '기획/운영');
INSERT INTO occupation_category (code, name) VALUES (4, '기타');

-- 직업 (Job Categories)
INSERT INTO job_category (code, name, occupation_id) VALUES (101, '프론트엔드 개발자', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (102, '백엔드 개발자', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (103, '풀스텍 개발자', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (104, 'AI/머신러닝 엔지니어', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (105, '클라우드 엔지니어', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (106, 'DevOps 엔지니어', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (107, '데이터 엔지니어', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (108, '모바일 앱 개발자', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (109, '임베디드 시스템 개발자', 1);
INSERT INTO job_category (code, name, occupation_id) VALUES (110, '블록체인 개발자', 1);

-- 디자인 직군
INSERT INTO job_category (code, name, occupation_id) VALUES (201, 'UI/UX 디자이너', 2);
INSERT INTO job_category (code, name, occupation_id) VALUES (202, '그래픽 디자이너', 2);
INSERT INTO job_category (code, name, occupation_id) VALUES (203, '웹디자이너', 2);

-- 기획/운영 직군
INSERT INTO job_category (code, name, occupation_id) VALUES (301, '프로젝트 매니저', 3);
INSERT INTO job_category (code, name, occupation_id) VALUES (302, '데이터 분석가', 3);
INSERT INTO job_category (code, name, occupation_id) VALUES (303, '마케터', 3);

-- 기타 직군
INSERT INTO job_category (code, name, occupation_id) VALUES (401, '학생', 4);
INSERT INTO job_category (code, name, occupation_id) VALUES (402, '취업 준비', 4);
INSERT INTO job_category (code, name, occupation_id) VALUES (403, '연구원', 4);
INSERT INTO job_category (code, name, occupation_id) VALUES (499, '기타', 4);
