import React, { useState } from 'react';
import basketIcon from '../../../../../assets/basket-icon.svg';
import basketIcon2 from '../../../../../assets/done.svg';

import './ButtonAddBasket.scss';
import Basket from "../../Basket";

const ButtonAddBasket = () => {
    const [state, setState] = useState(false);
    const [activePopup, setActivePopup] = useState<boolean>(false)

    return (
        <button
            className='buttonBasket'
            onClick={() => state
                ? setActivePopup(!activePopup)
                : setState(true)}>
            {
                state
                    ? <img onClick={() => setActivePopup(true)} src={basketIcon2} alt="#"/>
                    : <img src={basketIcon} alt="#"/>
            }
        </button>
    );
};

export default ButtonAddBasket;