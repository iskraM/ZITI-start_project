import React, { useState } from 'react';

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export default function Register() {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleEmailChange = (event: any) => {
        setEmail(event.target.value);
    }
    const handleUsernameChange = (event: any) => {
        setUsername(event.target.value);
    }
    const handlePasswordChange = (event: any) => {
        setPassword(event.target.value);
    }

    async function handleSubmit(event: any) {
        event.preventDefault();

        const rqBody = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: "Name",
                surname: "Surname",
                email: email,
                age: -1,
                username: username,
                password: password
            })
        };

        await fetch('http://localhost:7001/users/register', rqBody)
            .then(async response => {
                if (response.ok) {
                    const data = await response.json();

                    if (data.message === 'User created') {
                        window.location.href = '/login';
                    } else {
                        console.log(data.message);
                        toast.warn('Napaka pri registraciji!', {
                            position: toast.POSITION.BOTTOM_RIGHT
                        });
                    }
                } else {
                    console.log(response.status);
                    toast.warn(response.status, {
                        position: toast.POSITION.BOTTOM_RIGHT
                    });
                }
            })
            .catch(error => {
                console.log(error);
                toast.error('Prišlo je do napake!', {
                    position: toast.POSITION.BOTTOM_RIGHT
                });
            });
    }

    return (
        <>
            <div className="flex flex-col items-center mx-auto mt-6 md:h-screen lg:py-0">
                <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
                    <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
                        <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                            Registracija za nove uporabnike
                        </h1>
                        <form onSubmit={handleSubmit} className="space-y-4 md:space-y-6">
                            <div>
                                <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Vaš email</label>
                                <input onChange={handleEmailChange} type="email" name="email" id="email" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="name@domain.com" />
                            </div>
                            <div>
                                <label htmlFor="username" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Vaše uporabniško ime</label>
                                <input onChange={handleUsernameChange} type="text" name="username" id="username" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="username" />
                            </div>
                            <div>
                                <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Geslo</label>
                                <input onChange={handlePasswordChange} type="password" name="password" id="password" placeholder="••••••••" className="bg-gray-50 border border-gray-300 text-gray-900 sm:text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />
                            </div>
                            <button type="submit" className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800">Registrirajte se</button>
                            <p className="text-sm font-light text-gray-500 dark:text-gray-400">
                                Že imate račun? <a href="/login" className="font-medium text-blue-600 hover:underline dark:text-blue-500">Prijavite se</a>
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </>
    );
}