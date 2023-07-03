import React, {useState} from 'react';
import { useUser } from './useUser';
import axios from "axios";
import { useNavigate } from "react-router-dom";

export function Login() {
    const { setUserId } = useUser();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    let navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await axios.post(process.env.REACT_APP_BACKEND_URL + 'login', {
                id: -1,
                login: login,
                password: password,
            });
            if (response.data !== -1) {
                setUserId(response.data);
                console.log(response.data)
                navigate("/items");
            } else {
                window.alert("Niepoprawne dane logowania")
            }
        } catch (error) {
            console.error(error);
        }

    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Login"
                id="login"
                value={login}
                onChange={(e) => setLogin(e.target.value)}
            />
            <br />
            <input
                type="password"
                placeholder="Password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <br />
            <button id="log-in" type="submit">Log in</button>
        </form>
    );
}