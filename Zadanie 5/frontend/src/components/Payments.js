import React, { useEffect } from 'react';
import axios from "axios";
import {useUser} from "./useUser";

export default function Payments({orders, setOrders}) {
    const {userId} = useUser();

    useEffect(() => {
        axios.get(process.env.REACT_APP_BACKEND_URL + 'getPayments/' + userId)
            .then((res) => {
                setOrders(res.data)
            })
    }, [setOrders, userId]);

    const pay = (payment)=> {
        axios.put(process.env.REACT_APP_BACKEND_URL + 'pay', null, { params: { id: payment.id }})
            .then(res => console.log(res.data));
        let index = orders.findIndex(order => order.id === payment.id)
        orders[index].done = true
        setOrders(orders)
        window.alert("Zamówienie opłacone")
    }

    return (
        <div>{orders.map((payment) => (
            <div id={"payment-" + payment.id} key={payment.id}>
                <p> ID: {payment.id} </p>
                <p> Order ID: {payment.orderId} </p>
                <p> Total: ${payment.total} </p>
                {!payment.done && <button id={"pay-" + payment.id} onClick={() => pay(payment)}> Pay! </button>}
            </div>
        ))}
        </div>
    );
}