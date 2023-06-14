import './App.css';
import Cart from "./components/Cart";
import Homepage from "./components/Homepage";
import Items from "./components/Items";
import Payments from "./components/Payments";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import {useState} from "react";

function App() {

    const [cart, setCart] = useState([])
    const [orders, setOrders] = useState([])
    const [paidOrders, setPaidOrders] = useState([])
    return (
        <Router>
            <Homepage/>
            <Routes>
                <Route path="/" element={<Items cart = {cart} setCart = {setCart}/>}/>
                <Route path="/cart" element={<Cart cart = {cart} setCart = {setCart}/>}/>
                <Route path="/payments" element={<Payments orders = {orders} setOrders = {setOrders} paidOrders = {paidOrders} setPaidOrders = {setPaidOrders}/>}/>
            </Routes>
        </Router>
    );
}

export default App;