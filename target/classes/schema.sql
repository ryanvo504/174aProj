-- Create Students table
CREATE TABLE IF NOT EXISTS students (
    perm INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    dept VARCHAR(10) NOT NULL,
    pin INTEGER NOT NULL
);

-- Create Courses table
CREATE TABLE IF NOT EXISTS courses (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create Course Offerings table
CREATE TABLE IF NOT EXISTS course_offerings (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    year INTEGER NOT NULL,
    quarter VARCHAR(2) NOT NULL,
    professor VARCHAR(100) NOT NULL,
    capacity INTEGER NOT NULL,
    time_location VARCHAR(100) NOT NULL,
    enrollment INTEGER NOT NULL,
    FOREIGN KEY (code) REFERENCES courses(code),
    UNIQUE (code, year, quarter)
);

-- Create Prerequisites table
CREATE TABLE IF NOT EXISTS prerequisites (
    course_code VARCHAR(10) NOT NULL,
    prereq_code VARCHAR(10) NOT NULL,
    PRIMARY KEY (course_code, prereq_code),
    FOREIGN KEY (course_code) REFERENCES courses(code),
    FOREIGN KEY (prereq_code) REFERENCES courses(code)
);

-- Create Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
    id SERIAL PRIMARY KEY,
    student_perm INTEGER NOT NULL,
    offering_id INTEGER NOT NULL,
    grade VARCHAR(2),
    status VARCHAR(20) NOT NULL, -- 'enrolled', 'completed', 'dropped'
    FOREIGN KEY (student_perm) REFERENCES students(perm),
    FOREIGN KEY (offering_id) REFERENCES course_offerings(id)
);

-- Create Program Requirements table
CREATE TABLE IF NOT EXISTS program_requirements (
    course_code VARCHAR(10) NOT NULL,
    requirement_type VARCHAR(20) NOT NULL, -- 'required' or 'elective'
    PRIMARY KEY (course_code),
    FOREIGN KEY (course_code) REFERENCES courses(code)
); 