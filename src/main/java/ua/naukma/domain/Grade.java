package ua.naukma.domain;

import ua.naukma.server.repository.GetId;
import ua.naukma.server.repository.GetName;

import java.io.Serializable;

public class Grade implements Serializable, GetId, GetName {
    private int gradeId;
    private int score;
    private int studentId;
    //private Student student;
    private Subject subjectName;

    public Grade(int gradeId, int score, int studentId, Subject subjectName) {
        this.gradeId = gradeId;
        this.score = score;
        this.studentId = studentId;
        this.subjectName = subjectName;
    }
    @Override
    public int getId() {
        return gradeId;
    }

    @Override
    public String getName() {return subjectName.name(); }
    public int getScore() {
        return score;
    }

    public int getStudentId() {
        return studentId;
    }
    public Subject getSubjectName() {
        return subjectName;
    }

    public void setId(int gradeId) {
        this.gradeId = gradeId;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", score=" + score +
                ", studentId=" + studentId +
                ", subjectName=" + subjectName +
                '}';
    }
}
