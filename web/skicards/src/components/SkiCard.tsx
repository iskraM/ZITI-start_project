import React from 'react';
import { Card, CardHeader, CardBody, CardFooter } from '@material-tailwind/react'

interface SkiCardProps {
    type: string;
    price: number;
    description: string;
    image: string;
    click: any;
}


const SkiCard: React.FC<SkiCardProps> = ({ type, price, description, image, click }) => {
    function addToCart() {
        click(type, price.toFixed(2), 1);
    }

    return (
        <Card className='max-h-50'>
            <CardHeader floated={false} className=''>
                <img className="w-full h-40" src={image} alt="ski-type" />
            </CardHeader>
            <CardBody className='text-center'>
                <p className="font-bold text-3xl mb-2">{type}</p>
                <p className="text-lg litalic mb-2">{description}</p>
            </CardBody>
            <CardFooter divider className='flex items-center justify-between py-2'>
                <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-lg font-semibold text-gray-700">{price.toFixed(2)} €</span>
                <button className="w-20 items-center justify-content px-3 py-1 border border-transparent bg-blue-600 text-lg rounded-full font-bold text-white hover:bg-blue-700" onClick={addToCart}>Add</button>
            </CardFooter>
        </Card>
    );
};

export default SkiCard;