import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

export function Register() {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    let navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();

        axios.post('http://127.0.0.1:8080/register', {
            id: -1,
            login: login,
            password: password
        })
        .then(function (response) {
            if (response.data === false) {
                window.alert("Użytkownik o takim loginie istnieje")
            } else {
                navigate("/login");
            }
        })
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Login"
                value={login}
                onChange={(e) => setLogin(e.target.value)}
            />
            <br />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <br />
            <input type="submit" value="Register" />
        </form>
    );
}
