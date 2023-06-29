import React, { useEffect } from 'react';
import axios from "axios";
import {useUser} from "./useUser";

export default function Payments({orders, setOrders}) {
    const {userId} = useUser();

    useEffect(() => {
        axios.get('http://127.0.0.1:8080/getPayments/' + userId)
            .then((res) => {
                setOrders(res.data)
            })
    }, [setOrders, userId]);

    const pay = (payment)=> {
        axios.put('http://127.0.0.1:8080/pay', payment)
            .then(res => console.log(res.data));
        let index = orders.findIndex(order => order.id === payment.id)
        orders[index].done = true
        setOrders(orders)
        window.alert("Zamówienie opłacone")
    }

    return (
        <div>{orders.map((payment) => (
            <div key={payment.id}>
                <p> ID: {payment.id} </p>
                <p> Order ID: {payment.orderId} </p>
                <p> Total: ${payment.total} </p>
                {!payment.done && <button onClick={() => pay(payment)}> Pay! </button>}
            </div>
        ))}
        </div>
    );
}