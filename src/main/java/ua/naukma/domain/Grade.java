package ua.naukma.domain;

import java.io.Serializable;

public class Grade implements Serializable {
    private int gradeId;
    private int score;
    private int studentId;
    //private Student student;
    private Subject subjectName;

    public Grade(int score, int studentId, Subject subjectName) {
        setGradeId(gradeId);
        this.score = score;
        this.studentId = studentId;
        this.subjectName = subjectName;
    }

    public int getGradeId() {
        return gradeId;
    }
    public int getScore() {
        return score;
    }
//    public Student getStudent() {
//        return student;
//    }
    public int getStudentId() {
        return studentId;
    }
    public Subject getSubjectName() {
        return subjectName;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public String toString() {
        return "Grade " + score;
    }
}
