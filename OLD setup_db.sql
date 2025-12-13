CREATE DATABASE IF NOT EXISTS tasks_cs363;
USE tasks_cs363;

DROP TABLE IF EXISTS Organizations;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Managers;
DROP TABLE IF EXISTS WorksFor;
DROP TABLE IF EXISTS Projects;
DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS AssignedTasks;

CREATE TABLE Organizations (
    org_name VARCHAR(255) NOT NULL,
    PRIMARY KEY(org_name)
);

CREATE TABLE Users (
    user_id INT NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE Managers (
    user_id INT NOT NULL,
    expertise_area varchar(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE WorksFor (
    org_name varchar(255) NOT NULL,
    user_id INT NOT NULL,

    PRIMARY KEY (org_name, user_id),
    FOREIGN KEY (org_name) REFERENCES Organizations(org_name),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Projects (
    project_id INT NOT NULL AUTO_INCREMENT,
    project_name varchar(255) NOT NULL,
    org_name varchar(255) NOT NULL,
    manager_uid INT,

    PRIMARY KEY (project_id),
    FOREIGN KEY (org_name) REFERENCES Organizations(org_name),
    FOREIGN KEY (manager_uid) REFERENCES Managers(user_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Tasks (
    task_id INT NOT NULL AUTO_INCREMENT,
    project_id INT NOT NULL,
    title varchar(255) NOT NULL,
    descript varchar(255) NOT NULL,
    priority_lvl INT NOT NULL,
    due_date DATE,
    
    PRIMARY KEY (task_id),
    FOREIGN KEY (project_id) REFERENCES Projects(project_id)
);

CREATE TABLE AssignedTasks (
    user_id INT NOT NULL,
    task_id INT NOT NULL,

    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES Tasks(task_id) ON UPDATE CASCADE ON DELETE CASCADE
);

-- 

INSERT INTO Organizations VALUES
("Company A Inc."),
("Company B LLC."),
("Company C Corp.");

INSERT INTO Users VALUES
(DEFAULT, "Michael", "Jones", "mjones@gmail.com"),
(DEFAULT, "Jeremiah", "Bullfrog", "jb@outlook.com"),
(DEFAULT, "Stacy", "Stal", "sstal@gmail.com"),
(DEFAULT, "Suzanne", "Rey", "srey54@gmail.com"),
(DEFAULT, "DaQuavious", "Johnson", "dqj.449@gmail.com"),
(DEFAULT, "Horatio", "Ilkson", "horatio_ilkson@outlook.com"),
(DEFAULT, "Maya", "Sundaresh", "msund12@ishtar.org"),
(DEFAULT, "Xivu", "Arath", "godofwar@hive.gov"),
(DEFAULT, "Lilly", "Bloom", "lbloom23@gmail.com"),
(DEFAULT, "Lilac", "Bloom", "lbloom25@gmail.com"),
(DEFAULT, "Chioma", "Essi", "cessi@ishtar.org"),
(DEFAULT, "Stan", "Ace", "stan.ace@outlook.com"),
(DEFAULT, "Monika", "Rey", "mrey21@gmail.com"),
(DEFAULT, "Christina", "Shmistina", "cs4444@gmail.com");

INSERT INTO Managers VALUES
(2, "User Experience"),
(4, "Art"),
(6, "Biology & Life Sciences"),
(12, "Systems Engineering");

INSERT INTO WorksFor VALUES
();

INSERT INTO Projects VALUES
(DEFAULT, "Artwork for Clients", "Company A Inc.", 4), -- 1
(DEFAULT, "New Rendering Pipeline Development", "Company A Inc.", NULL), -- 2
(DEFAULT, "Tag Library", "Company B LLC.", NULL), -- 3
(DEFAULT, "New Website", "Company B LLC.", 2), -- 4
(DEFAULT, "Arm translation Layer for x86", "Company B LLC.", 12), -- 5
(DEFAULT, "Lab Renovation", "Company C Corp.", NULL), -- 6
(DEFAULT, "Research into Exo Frames", "Company C Corp.", 6);  -- 7

INSERT INTO Tasks VALUES (DEFAULT, 1, "Title", "desc", 0, "2026-06-25");
INSERT INTO AssignedTasks VALUES ();