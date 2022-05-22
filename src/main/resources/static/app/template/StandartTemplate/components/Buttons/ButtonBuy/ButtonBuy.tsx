import React from 'react';
import './ButtonBuy.scss'

const ButtonBuy = ({text = 'ПОЛУЧИТЬ', border= false}) => {
    return (
        <button className={border ? "border" + " button": "button" }>{ text }</button>
    );
};

export default ButtonBuy;