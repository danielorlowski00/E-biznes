import React from "react";
import { NavLink as Link } from "react-router-dom";

function Homepage() {
    return (
        <div>
            <Link to="/" className={"items"}> Items </Link>{' '}
            <span>|</span>{' '}
            <Link to="/cart"> Cart </Link>{' '}
            <span>|</span>{' '}
            <Link to="/payments"> Payments </Link>
        </div>
    );
}

export default Homepage;