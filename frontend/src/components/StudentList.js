import React from "react";
import axios from "axios";

const StudentList = ({ students, fetchStudents }) => {
    const deleteStudent = async (id) => {
        await axios.delete(`http://localhost:5000/api/students/${id}`);
        fetchStudents();
    };

    return (
        <ul>
            {students.map(student => (
                <li key={student.id}>
                    {student.name} - {student.course}
                    <button onClick={() => deleteStudent(student.id)}>Delete</button>
                </li>
            ))}
        </ul>
    );
};

export default StudentList;