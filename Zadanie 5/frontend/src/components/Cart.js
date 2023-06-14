import React from 'react';
import axios from "axios";

export default function Cart({cart, setCart}){

    const deleteItemFromCart = (itemId) => {
        setCart(cart.filter(order => order.itemId !== itemId));
    }
    const finalizeOrder = (cart)=> {
        axios.post('http://127.0.0.1:8080/addOrder', cart).then(res => console.log(res))
        setCart([])
    }

    return (
        <div>
            {cart.map((order) => (
                <div key={order.id}>
                    <p> Item ID: {order.itemId} </p>
                    <p> Quantity: {order.quantity} </p>
                    <button onClick={() => deleteItemFromCart(order.itemId)}> Delete from cart </button>
                </div>
            ))}
            {cart.length > 0 && <button onClick={()=> finalizeOrder(cart)}> Order! </button>}
        </div>
    );
}