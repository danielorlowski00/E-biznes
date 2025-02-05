import React, { useRef, useState, useEffect } from 'react';
import axios from "axios";
import { Cart } from "../models/Cart";
import {useUser} from "./useUser";


export default function Items({cart,setCart}){
    
    const {userId} = useUser();
    const [items, setItems] = useState([]);

    const [itemInCart,setItemInCart] = useState(Cart)
    const [quantity, setQuantity] = useState(0)
    const itemBought= useRef(false)


    useEffect(() => {
        axios.get(process.env.REACT_APP_BACKEND_URL + 'getItems')
            .then((res) => {
                setItems(res.data)
            })
    }, []);

    useEffect(() => {
        if (itemBought.current) {
            let order =  {
                id: itemInCart.id,
                itemId: itemInCart.itemId,
                quantity: itemInCart.quantity,
                orderId: 0,
                userId: userId
            }
            cart.isEmpty ? setCart(order) : setCart(cart.concat(order))
            itemBought.current = false
        }
    }, [cart, itemInCart.id, itemInCart.itemId, itemInCart.quantity, setCart, userId])

    const addToCart = (item)=> {
        let index = cart.findIndex(itemInCart => itemInCart.itemId === item.id)
        if (index === -1) {
            let order = Cart
            order.id = item.id
            order.itemId = item.id
            order.quantity = 1
            order.orderId = 0
            setItemInCart(order)
            itemBought.current = true
            setQuantity(quantity + 1)
        } else {
            cart[index].quantity += 1
        }
    }
    return (
        <div>{items.map((item) => (
            <div id={"item-" + item.id} key={item.id}>
                <h3> {item.name} </h3>
                <p> ID: {item.id} </p>
                <p> Category: {item.categoryName} </p>
                <p> Price: ${item.price} </p>
                <button id={"item-" + item.id + "-add"} onClick={() => addToCart(item)}> Add to cart! </button>
            </div>
        ))}
        </div>
    );
}