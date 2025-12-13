CREATE DATABASE IF NOT EXISTS tasks_cs363;
USE tasks_cs363;

DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS Projects;
DROP TABLE IF EXISTS Managers;
DROP TABLE IF EXISTS Users;

-- Users(u_id, first_name, last_name, email, addr)
-- Managers(u_id, expertise_area, exp_years)
-- Projects(p_id, project_name, manager_uid)
-- Tasks(task_id, project_id, title, descript, priority_lvl, due_date)

CREATE TABLE Users (
    user_id INT NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    addr varchar(255),
    PRIMARY KEY(user_id)
);

CREATE TABLE Managers (
    user_id INT NOT NULL,
    expertise_area varchar(255) NOT NULL,
    exp_years INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Projects (
    project_id INT NOT NULL AUTO_INCREMENT,
    project_name varchar(255) NOT NULL,
    manager_uid INT,

    PRIMARY KEY (project_id),
    FOREIGN KEY (manager_uid) REFERENCES Managers(user_id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Tasks (
    task_id INT NOT NULL AUTO_INCREMENT,
    project_id INT NOT NULL,
    user_id INT,
    title varchar(255) NOT NULL,
    descript varchar(255) NOT NULL,
    priority_lvl INT NOT NULL,
    due_date DATE,
    
    PRIMARY KEY (task_id),
    FOREIGN KEY (project_id) REFERENCES Projects(project_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON UPDATE CASCADE ON DELETE SET NULL
);

--

INSERT INTO Users VALUES
(DEFAULT, "Michael", "Jones", "mjones@gmail.com", "10 A Street"),
(DEFAULT, "Jeremiah", "Bullfrog", "jb@outlook.com", "12 B Street"),
(DEFAULT, "Stacy", "Stal", "sstal@gmail.com", "14 B Street"),
(DEFAULT, "Suzanne", "Rey", "srey54@gmail.com", "465 C Lane"),
(DEFAULT, "DaQuavious", "Johnson", "dqj.449@gmail.com", "9A Summer Road"),
(DEFAULT, "Horatio", "Ilkson", "horatio_ilkson@outlook.com", "2 Duck Lane"),
(DEFAULT, "Maya", "Sundaresh", "msund12@ishtar.org", "124 Sesame Street"),
(DEFAULT, "Xivu", "Arath", "war@hive.gov", "88 Saturn Way"),
(DEFAULT, "Lilly", "Bloom", "lbloom23@gmail.com", "23 Lands's End Lane"),
(DEFAULT, "Lilac", "Bloom", "lbloom25@gmail.com", "25 Lands's End Lane"),
(DEFAULT, "Chioma", "Essi", "cessi@ishtar.org", "14 Ishtar Road"),
(DEFAULT, "Monika", "Rey", "mrey21@gmail.com", "99 Star Road");

INSERT INTO Managers VALUES
(3, "User Experience", 5),
(6, "Art", 2),
(9, "Frontend Development", 3),
(12, "Systems Engineering", 10);

INSERT INTO Projects VALUES
(DEFAULT, "New App Theme", 3), -- 1
(DEFAULT, "Concept Artwork for Clients", 6), -- 2
(DEFAULT, "New Website", 9), -- 3
(DEFAULT, "New Rendering Pipeline Development", NULL); -- 4

INSERT INTO Tasks VALUES (DEFAULT, 1, 1, "Research Trends", "Research industry tends to understand design languages", 0, "2026-5-31");
INSERT INTO Tasks VALUES (DEFAULT, 1, 2, "Create Mockups", "Create possible mockups of system", 2, "2026-06-24");
INSERT INTO Tasks VALUES (DEFAULT, 1, 3, "Review Mockups", "Reach out to client to gather requirements", 2, "2026-06-25");
INSERT INTO Tasks VALUES (DEFAULT, 1, 3, "Get Feedback", "Reach out to client to get feedback on internal mockups", 2, "2026-06-30");

INSERT INTO Tasks VALUES (DEFAULT, 2, 4, "Create Draft", "Create drafts of artwork for clients", 1, "2026-5-25");
INSERT INTO Tasks VALUES (DEFAULT, 2, 5, "Review Drafts", "Review created drafts of client artwork", 2, "2026-5-27");
INSERT INTO Tasks VALUES (DEFAULT, 2, 6, "Ask Clients", "Ask clients for required information", 0, "2026-5-20");

INSERT INTO Tasks VALUES (DEFAULT, 3, 7, "Gather Feedback", "Implement poll on existing website for gathering user information", 0, "2026-5-10");
INSERT INTO Tasks VALUES (DEFAULT, 3, 8, "Create Site Mockup", "Create a site mockup based on internal data", 0, "2026-5-10");
INSERT INTO Tasks VALUES (DEFAULT, 3, 9, "Review Mockup", "Using the created mockup and data from poll, determine changes to be made", 1, "2026-5-24");

INSERT INTO Tasks VALUES (DEFAULT, 4, 10, "Conduct Research", "Review research and determine possible improvements", 0, "2026-4-30");
INSERT INTO Tasks VALUES (DEFAULT, 4, 12, "Conduct Research", "Review research and determine possible improvements", 0, "2026-4-30");