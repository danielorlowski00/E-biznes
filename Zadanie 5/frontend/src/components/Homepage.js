import React from "react";
import { NavLink as Link} from "react-router-dom";
import {useUser} from "./useUser";

function Homepage() {
    const { userId, setUserId } = useUser();

    const handleSignOut = () => {
        setUserId(undefined);
    };

    return (
        <div>
            {userId !== undefined && <Link to="/items" id="items-nav"> Items </Link>}
            {userId !== undefined && <Link to="/cart" id="cart-nav"> Cart </Link>}
            {userId !== undefined && <Link to="/payments" id="payments-nav"> Payments </Link>}
            {userId === undefined && <Link to="/" id="register-nav"> Register </Link>}
            {userId === undefined && <Link to="/login" id="login-nav"> Log In </Link>}
            {userId !== undefined && <Link to="/login" id="signout-nav" onClick={handleSignOut}> Sign Out </Link>}
        </div>
    );
}

export default Homepage;