import React, { useState, useEffect } from "react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function Profile() {
    interface Order {
        id: string;
        orderDate: string;
        price: number;
        quantity: number;
    }
    const [orders, setOrders] = useState<Order[]>([]);

    interface SkiCard {
        id: string;
        type: string;
        code: string;
        is_active: boolean;
        activate_date: string | null;
        physical_card: string | null;
    }
    const [skiCards, setSkiCards] = useState<SkiCard[]>([]);
    const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);

    useEffect(() => {
        const userId = sessionStorage.getItem("_id");

        fetch("http://localhost:7001/orders/owned/" + userId)
            .then((response) => response.json())
            .then((data) => setOrders(data))
            .catch((error) => console.error(error));
    }, []);

    return (
        <div className="px-4 py-3 ">
            <ToastContainer />
            <p className="text-3xl mb-4 text-center">Pozdravljen {sessionStorage.getItem("username")}!</p>
            {
                orders.length > 0 ? (
                    <>
                        <h2 className="mb-2">Vaša naročila:</h2>
                        {orders.map((order, index) => (
                            <div key={index} className="border border-color-black rounded-lg mb-2">
                                <div className="flex px-3 py-1">
                                    <div className="flex-1 mr-10">
                                        <p>Datum naročila: {new Date(order.orderDate).toLocaleDateString()}</p>
                                        <div className="flex justify-between px-4 py-2">
                                            <p>Število naročenih kart: {order.quantity}</p>
                                            <p>Cena: {order.price}</p>
                                        </div>
                                    </div>
                                    {
                                        selectedOrderId !== order.id ? (
                                            <button onClick={() => onOrderClick(order.id)} type="button" className="my-auto h-1/2 text-blue-700 border border-blue-700 hover:bg-blue-700 hover:text-white focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-full text-sm p-2.5 text-center inline-flex items-center dark:border-blue-500 dark:text-blue-500 dark:hover:text-white dark:focus:ring-blue-800 dark:hover:bg-blue-500">
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 8.25l-7.5 7.5-7.5-7.5" /></svg>
                                                <span className="sr-only">Icon description</span>
                                            </button>
                                        ) : (
                                            <button onClick={() => { if (selectedOrderId == order.id) setSelectedOrderId(null) }} type="button" className="my-auto h-1/2 text-blue-700 border border-blue-700 hover:bg-blue-700 hover:text-white focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-full text-sm p-2.5 text-center inline-flex items-center dark:border-blue-500 dark:text-blue-500 dark:hover:text-white dark:focus:ring-blue-800 dark:hover:bg-blue-500">
                                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M4.5 15.75l7.5-7.5 7.5 7.5" /></svg>
                                                <span className="sr-only">Icon description</span>
                                            </button>
                                        )
                                    }
                                </div>

                                {selectedOrderId === order.id && skiCards.length == order.quantity && (
                                    <div>
                                        <hr />
                                        <p className="text-2xl text-center mt-4">Naročene smučarske karte</p>
                                        {
                                            skiCards.map((skiCard, index) => (
                                                <div key={skiCard.id} className="px-3 py-2 border-t-2">
                                                    <p>Tip karte: {skiCard.type}</p>
                                                    <div className="flex gap-6 items-center">
                                                        <p className="w-1/3">Koda kupljene karte: <b>{skiCard.code}</b></p>
                                                        {
                                                            skiCard.is_active ? (
                                                                <p className="flex-1 text-center">Aktivirana: <b>{new Date(skiCard.activate_date!).toLocaleDateString()}</b></p>
                                                            ) : (
                                                                <>
                                                                    <label className="relative block flex-auto">
                                                                        <span className="absolute inset-y-0 left-0 flex items-center pl-2">
                                                                            <svg className="h-5 w-5 fill-gray-400" viewBox="0 0 24 24" fill="currentColor" height="1em" width="1em">
                                                                                <path fill="none" d="M0 0h24v24H0z" />
                                                                                <path d="M2 4h2v16H2V4zm4 0h1v16H6V4zm2 0h2v16H8V4zm3 0h2v16h-2V4zm3 0h2v16h-2V4zm3 0h1v16h-1V4zm2 0h3v16h-3V4z" />
                                                                            </svg>
                                                                        </span>
                                                                        <input
                                                                            className="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm"
                                                                            placeholder="Koda fizične kartice"
                                                                            name={`physical_card_${index}`}
                                                                            type="text"
                                                                            maxLength={11}
                                                                            onChange={(e) => {
                                                                                e.target.value = e.target.value.toUpperCase();
                                                                            }}
                                                                        />
                                                                    </label>
                                                                    <button onClick={() => activateSkiCard(index)} type="button" className="w-40 text-white bg-blue-700 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 font-medium rounded-full font-extrabold text-sm px-2 py-2 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                                                                        AKTIVIRAJ
                                                                    </button>
                                                                </>
                                                            )
                                                        }
                                                    </div>
                                                </div>
                                            ))
                                        }
                                    </div>
                                )}
                            </div>
                        ))}
                    </>
                ) : (
                    <p>Nimate obstoječih naročil.</p>
                )
            }
        </div>
    );

    function onOrderClick(orderId: string) {
        setSelectedOrderId(orderId);
        setSkiCards([]);

        fetch("http://localhost:7001/orders/cards/" + orderId)
            .then((response) => response.json())
            .then((data) => setSkiCards(data))
            .catch((error) => console.error(error));
    }

    function activateSkiCard(skiCardId: number) {
        const activation_for_card = skiCards[skiCardId];
        const physical_code = (document.getElementsByName(`physical_card_${skiCardId}`)[0] as HTMLInputElement).value;

        console.log(activation_for_card.code);
        console.log(physical_code);

        if (physical_code.length === 0) {
            toast.warn('Vnesite kodo fizične kartice!', {
                position: toast.POSITION.BOTTOM_RIGHT
            });
            return;
        }

        fetch(`http://localhost:7001/skicards/${activation_for_card.code}/${physical_code}`, { method: 'PUT' })
            .then((response) => {
                if (response.ok) {
                    console.log(response);
                    toast.success('Karta je bila uspešno aktivirana!', {
                        position: toast.POSITION.BOTTOM_RIGHT
                    });
                    onOrderClick(selectedOrderId!);
                } else {
                    toast.error('Prišlo je do napake!', {
                        position: toast.POSITION.BOTTOM_RIGHT
                    });
                }
            })
            .catch((error) => {
                console.error(error);
                toast.error('Prišlo je do napake!', {
                    position: toast.POSITION.BOTTOM_RIGHT
                });
            });
    }
}