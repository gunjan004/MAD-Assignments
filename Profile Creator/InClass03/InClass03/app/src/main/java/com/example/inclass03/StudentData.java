package com.example.inclass03;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentData implements Parcelable {

    String firstName;
    String lastName;
    String studentId;
    String department;
    int imageId;

    public StudentData(String firstName, String lastName, String studentId, String department, int imageId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.department = department;
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "StudentData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", department='" + department + '\'' +
                ", imageId=" + imageId +
                '}';
    }

    protected StudentData(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.studentId = in.readString();
        this.department = in.readString();
        this.imageId = in.readInt();
    }

    public static final Creator<StudentData> CREATOR = new Creator<StudentData>() {
        @Override
        public StudentData createFromParcel(Parcel in) {
            return new StudentData(in);
        }

        @Override
        public StudentData[] newArray(int size) {
            return new StudentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.studentId);
        dest.writeString(this.department);
        dest.writeInt(this.imageId);
    }
}
