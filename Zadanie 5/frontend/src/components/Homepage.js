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
            {userId !== undefined && <Link to="/items" className={"items"}> Items </Link>}
            {userId !== undefined && <Link to="/cart"> Cart </Link>}
            {userId !== undefined && <Link to="/payments"> Payments </Link>}
            {userId === undefined && <Link to="/"> Register </Link>}
            {userId === undefined && <Link to="/login"> Log In </Link>}
            {userId !== undefined && <Link to="/" onClick={handleSignOut}> Sign Out </Link>}
        </div>
    );
}

export default Homepage;