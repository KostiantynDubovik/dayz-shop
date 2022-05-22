import React from 'react';
import basket from "../../../../../assets/basket-icon.svg";
import './ButtonBasket.scss';
import {IButtonBasket} from "./interfaces";

const ButtonBasket = ({count, onClick}: IButtonBasket) => {
    return (
        <button onClick={onClick} className='basket button-default '>
            <img className='iconBasket' src={basket} alt="#"/>
            { !!count && <div className='countBasket'>{count}</div> }
        </button>
    );
};

export default ButtonBasket;