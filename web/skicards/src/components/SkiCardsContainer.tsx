import React, { useState } from "react";
import SkiCard from "./SkiCard";
import { Button } from "@material-tailwind/react";

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


export default function SkiCardsContainer() {
    const cards = [
        { type: "Otroci", price: 19.99, description: "Otroci do 17 let.", image: "https://checkyeti.imgix.net/images/prod/products/3920/kids-ski-lessons-4-12-years-all-levels-skischule-zugspitze-grainau-hero.jpg" },
        { type: "Odrasli", price: 31.99, description: "Odrasli.", image: "https://www.tripsavvy.com/thmb/rvzhj-47PRJmWFcx_8FHO_sYfHE=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/171682512-56a87f755f9b58b7d0f2e093.jpg" },
        { type: "Seniorji", price: 26.99, description: "Starejši od 60 let.", image: "https://thepointsguy.global.ssl.fastly.net/us/originals/2019/01/Family-Ski-Trips-Colorado_Hull-9.jpg" }
    ]

    interface BasketItem {
        type: string;
        price: number;
        count: number;
    }
    const [basket, setBasket] = useState<BasketItem[]>([]);
    const handleRemoveItem = (index: number) => {
        const newBasket = basket.filter((item, i) => i !== index);
        setBasket(newBasket);

        toast.info("Izdelek odstranjen iz košarice!", {
            position: toast.POSITION.BOTTOM_RIGHT
        });
    }

    function myClick(type: string, price: number, count: number) {
        const newBasketItem: BasketItem = {
            type: type,
            price: price,
            count: count
        }

        setBasket([...basket, newBasketItem]);
    }

    return (
        <div>
            <ToastContainer />
            <p className="text-center text-neutral-800 text-2xl mt-3 mb-4">Cenik smučarskih vozovnic</p>

            <div className="grid grid-cols-4 gap-4">
                {cards.map((card) => (
                    <SkiCard click={myClick} type={card.type} price={card.price} description={card.description} image={card.image} />
                ))}

                <div className="border border-color-black pl-2 pr-2 rounded-lg">
                    <h2 className="mb-2">Košarica:</h2>
                    <hr />
                    {
                        basket.length > 0 ? (
                            <div className="mb-1" style={{ display: 'flex', flexDirection: 'column', height: '65%', maxHeight: '230px' }}>
                                <div style={{ height: '100%', overflowY: 'scroll' }}>
                                    {basket.map((item, index) => (
                                        <div key={index}>
                                            <p className="text-lg"><Button className="px-4 rounded-full text-color-black border-color-black" variant="outlined" onClick={() => handleRemoveItem(index)}>X</Button> | {item.count} x {item.type}</p>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ) : (
                            <p className="text-sm text-center">Košarica je prazna.</p>
                        )
                    }
                    {
                        basket.length > 0 ? (
                            <div>
                                <p className="text-lg font-bold text-center">Skupaj: {basket.reduce((acc, item) => acc + item.price * 1, 0).toFixed(2)} €</p>
                                <Button className="w-full rounded-full" variant="gradient" onClick={placeOrder}>Nakup</Button>
                            </div>
                        ) : (
                            <div />
                        )
                    }
                </div>
            </div>
        </div>
    );

    async function placeOrder() {
        const rqBody = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userID: sessionStorage.getItem("_id"),
                createSkiCards: basket.map((item) => ({
                    active_time: 8,
                    type: item.type,
                    price: item.price,
                }))
            })
        };

        await fetch('http://localhost:7001/orders', rqBody)
            .then(response => {
                if (response.status === 200) {
                    toast.success("Nakup uspešen!", {
                        position: toast.POSITION.BOTTOM_RIGHT
                    });
                    setBasket([]);
                } else {
                    console.log(response.status);
                    toast.error("Med opravljanjem nakupa je prišlo do napake!", {
                        position: toast.POSITION.BOTTOM_RIGHT
                    });
                }
            });
    }
};