-- id값 테이블은 1부터, enum은 0부터
---- 관심 분야
INSERT INTO interest (name, code) VALUES ('데이터 분석 / AI', 101);
INSERT INTO interest (name, code) VALUES ('클라우드 / DevOps', 102);
INSERT INTO interest (name, code) VALUES ('소프트웨어 개발', 103);
INSERT INTO interest (name, code) VALUES ('디자인', 104);
INSERT INTO interest (name, code) VALUES ('정보 보안', 105);
INSERT INTO interest (name, code) VALUES ('신기술 연구', 106);
INSERT INTO interest (name, code) VALUES ('커리어 개발', 107);
INSERT INTO interest (name, code) VALUES ('기획/운영', 108);

-- 직군 (Job Group)
INSERT INTO job_group (code, name) VALUES (1, '개발');
INSERT INTO job_group (code, name) VALUES (2, '디자인');
INSERT INTO job_group (code, name) VALUES (3, '기획/운영');
INSERT INTO job_group (code, name) VALUES (4, '기타');

-- 직무 (Job Position)
INSERT INTO job_position (code, name, job_group_id) VALUES (101, '프론트엔드 개발자', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (102, '백엔드 개발자', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (103, '풀스텍 개발자', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (104, 'AI/머신러닝 엔지니어', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (105, '클라우드 엔지니어', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (106, 'DevOps 엔지니어', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (107, '데이터 엔지니어', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (108, '모바일 앱 개발자', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (109, '임베디드 시스템 개발자', 1);
INSERT INTO job_position (code, name, job_group_id) VALUES (110, '블록체인 개발자', 1);

-- 디자인 직군
INSERT INTO job_position (code, name, job_group_id) VALUES (201, 'UI/UX 디자이너', 2);
INSERT INTO job_position (code, name, job_group_id) VALUES (202, '그래픽 디자이너', 2);
INSERT INTO job_position (code, name, job_group_id) VALUES (203, '웹디자이너', 2);

-- 기획/운영 직군
INSERT INTO job_position (code, name, job_group_id) VALUES (301, '프로젝트 매니저', 3);
INSERT INTO job_position (code, name, job_group_id) VALUES (302, '데이터 분석가', 3);
INSERT INTO job_position (code, name, job_group_id) VALUES (303, '마케터', 3);

-- 기타 직군
INSERT INTO job_position (code, name, job_group_id) VALUES (401, '학생', 4);
INSERT INTO job_position (code, name, job_group_id) VALUES (402, '취업 준비', 4);
INSERT INTO job_position (code, name, job_group_id) VALUES (403, '연구원', 4);
INSERT INTO job_position (code, name, job_group_id) VALUES (499, '기타', 4);

-- 관리자
INSERT INTO admin (admin_auth_code) VALUES ('ADM12345');
INSERT INTO admin (admin_auth_code) VALUES ('ADM67890');

-- 채용 담당자
INSERT INTO recruiter (recruiter_auth_code, company, responsibility, name) VALUES ('RC12345', 'CodeSphere', 'HR팀 매니저', '박수진');
INSERT INTO recruiter (recruiter_auth_code, company, responsibility, name) VALUES ( 'RC67890', 'OpenStack Korea', 'HR팀 매니저', '김주은');

-- 참가자 기본 데이터
INSERT INTO attendee (email, password, name, phone, total_points, membership_level_type)
VALUES
    ('jiwon.kim@example.com', '$2a$10$aO4mzbreIOHJiJDgPaUtG.BS81l7i92I2.D2qkwvM5hvUB8BGBsk2', '김지원', '01012345678', 250, 'BRONZE'),
    ('youngho.choi@example.com', '$2a$10$hashedpassword2', '최영호', '01056781234', 1200, 'GOLD'),
    ('seoyeon.jung@example.com', '$2a$10$hashedpassword3', '정서연', '01055556666', 1200, 'GOLD'),
    ('sihyung.park@example.com', '$2a$10$hashedpassword4', '박시형', '01077778888', 300, 'SILVER'),
    ('dayoung.lee@example.com', '$2a$10$hashedpassword5', '이다영', '01099990000', 1500, 'GOLD'),
    ('dahye.kim@example.com', '$2a$10$hashedpassword6', '김다혜', '01011112222', 50, 'DEFAULT');

-- 김지원 (attendee_id = 1) → 수도권, 부산
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (1, 0); -- CAPITAL_AREA
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (1, 1); -- BUSAN

-- 최영호 (attendee_id = 2) → 대구
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (2, 2); -- DAEGU

-- 정서연 (attendee_id = 3) → 대전, 세종
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (3, 3); -- DAEJEON
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (3, 6); -- SEJONG

-- 박시형 (attendee_id = 4) → 강원, 전라
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (4, 7); -- GANGWON
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (4, 9); -- JEOLLA

-- 이다영 (attendee_id = 5) → 경상, 제주
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (5, 10); -- GYEONGSANG
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (5, 11); -- JEJU

-- 김다혜 (attendee_id = 6) → 울산, 충청
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (6, 5); -- ULSAN
INSERT INTO attendee_desired_work_region (attendee_id, desired_work_region) VALUES (6, 8); -- CHUNGCHEONG



-- 참가자의 현재 직업 및 직무 업데이트
UPDATE attendee
SET
is_hiring_interested = 1,
current_job_position_id = 2,
current_job_group_id = 1,
desired_job_group_id = 1,
desired_job_position_id = 2,
self_introduction = '저는 최신 기술을 배우고 IT 업계에서 성장하기 위해 다양한 컨퍼런스와 개발 프로젝트에 참여해왔습니다. 컴퓨터공학 전공자로서 웹 애플리케이션 개발과 데이터베이스 설계 경험이 있으며, 협업과 문제 해결 역량을 갖추고 있습니다. 여러 해커톤과 오픈소스 프로젝트에 참여하며 실무 감각을 익혔고, 컨퍼런스를 통해 업계 트렌드를 학습하며 네트워킹을 적극적으로 활용해왔습니다. 앞으로 데이터 분석과 AI 기술을 활용하여 가치 있는 서비스를 개발하고, 변화하는 환경 속에서 끊임없이 성장하는 개발자가 되고 싶습니다. 이번 기회를 통해 실무 경험을 쌓으며, IT 업계에서 더욱 발전할 수 있도록 최선을 다하겠습니다.',
tech_stacks = 'Java, AWS, Spring Boot, MySQL, Docker, JPA, github-actions, SonarQube, Redis, junit5, Mockito, Git',
age_group = 'AGE_20_24',
education_level = 2,
experience_level = 1
WHERE attendee_id = 1;

UPDATE attendee
SET
is_hiring_interested = 1,
tech_stacks = 'AWS',
age_group = 'AGE_20_24',
education_level = 2,
experience_level = 2,
current_job_position_id = 12,
current_job_group_id = 2,
desired_job_group_id = 2,
desired_job_position_id = 12,
self_introduction = '최신 사람입니다!'
WHERE attendee_id = 2;

UPDATE attendee
SET
is_hiring_interested = 1,
tech_stacks = 'Ruby',
age_group = 'AGE_25_29',
education_level = 1,
experience_level = 1,
current_job_position_id = 11,
current_job_group_id = 2,
desired_job_group_id = 2,
desired_job_position_id = 10,
self_introduction = '사람입니다!'
WHERE attendee_id = 3;

UPDATE attendee
SET
is_hiring_interested = 1,
tech_stacks = 'Go, C++',
age_group = 'AGE_35_PLUS',
education_level = 3,
experience_level = 3,
current_job_position_id = 1,
current_job_group_id = 1,
desired_job_group_id = 1,
desired_job_position_id = 2,
self_introduction = '혼자서 할 수 있는 개발자!'
WHERE attendee_id = 4;

UPDATE attendee
SET
is_hiring_interested = 1,
tech_stacks = 'Go, C++',
age_group = 'AGE_35_PLUS',
education_level = 3,
experience_level = 3,
current_job_position_id = 1,
current_job_group_id = 1,
desired_job_group_id = 1,
desired_job_position_id = 2,
self_introduction = '혼자서 할 수 있는 개발자!'
WHERE attendee_id = 5;

UPDATE attendee
SET
is_hiring_interested = 1,
tech_stacks = 'Git, Docker',
age_group = 'AGE_30_34',
education_level = 3,
experience_level = 3,
current_job_position_id = 1,
current_job_group_id = 1,
desired_job_group_id = 1,
desired_job_position_id = 2,
self_introduction = '100인 분 할 수 있는 개발자!'
WHERE attendee_id = 6;

-- 참가자의 관심 분야(Interest) 매핑
INSERT INTO attendee_interest (attendee_id, interest_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 4),
    (3, 5),
    (4, 6),
    (5, 7),
    (6, 8);


-- 채용담당자 좋아요 업데이트
INSERT INTO recruiter_attendee_like (id, attendee_id, recruiter_id)
    VALUES
        (1, 1, 1),
        (2, 2, 1),
        (3, 3, 1),
    (4, 1, 2),
    (5, 2, 2);

-- 참가자의 선호 기업 문화 업데이트
INSERT INTO attendee_preferred_corporate_cultures (attendee_id, preferred_corporate_cultures)
VALUES
    (1, 0),
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 0),
    (6, 1);

-- 참가자의 컨퍼런스 참여 목적
INSERT INTO attendee_conference_participation_purposes (attendee_id, conference_participation_purposes)
VALUES
    (1, 0),
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 0),
    (5, 2),
    (6, 3);

-- 직장 선택 요소
INSERT INTO attendee_workplace_selection_factors (attendee_id, workplace_selection_factors)
VALUES
    (1, 0),
    (1, 4),
    (2, 1),
    (3, 2),
    (4, 3),
    (5, 4),
    (6, 0);



-- 컨퍼런스
INSERT INTO conference (start_date, end_date, start_time, end_time, organizer, name, type, location, position)
VALUES
    ('2025-09-15', '2025-09-16','09:00', '18:00', 'FlowLink', 'F’LINK 2025', 'IT', '그랜드볼룸', '로비 A');


-- 부스
INSERT INTO booth (conference_id, company_name, company_type, booth_location, booth_number, booth_description, image_key, image_url, qr_key, qr_url, secret_code)
VALUES (1, 'CodeSphere', 'YourCompanyType', 'C HALL', '101C', '클라우드서비스: 글로벌 IT 기업 CodeSphere에서 React 기반 프론트엔드 엔지니어와 클라우드 기반 백엔드 엔지니어를 채용합니다. TypeScript, Node.js, Kubernetes 경험자를 환영합니다.', 'default-key', 'https://default-image-url.com/default.jpg', 'default-qr-key', 'https://default-qr-url.com/default-qr.png', 'default-secret-code');

-- 세션
INSERT INTO session (maximum, progress_date, conference_id, end_time, start_time, speaker_position, speaker, title, description, qr_key, qr_url, image_key, image_url, secret_code)
VALUES
    (250, '2025-09-15', 1, '2025-09-15 11:30', '2025-09-15 10:30', 'CTO', '김지혁', '최신 기술 동향', '빠르게 변화하는 IT 산업에서 최신 기술 동향을 파악하는 것은 기업의 경쟁력을 높이고 미래를 준비하는 데 필수적입니다. 기업의 CTO가 AI, 클라우드, Web3 등 주요 기술 트렌드와 산업 변화를 분석하고, 기업이 기술 혁신을 어떻게 주도할 수 있는지에 대한 전략과 인사이트를 제공합니다.', ' ', ' ', ' ', ' ', ' ');



