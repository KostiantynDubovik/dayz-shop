import React, {Dispatch, SetStateAction} from 'react';
import './Basket.scss';
import BasketCard from "./compoents/BasketCard";
import Popup from "../../../../shared/Popup";

interface IBasket {
    activePopup: boolean,
    setActivePopup: Dispatch<SetStateAction<boolean>>
}

const Basket = ({activePopup, setActivePopup}: IBasket ) => {
    return (
        <Popup activePopup={activePopup} setActivePopup={setActivePopup}>
            <BasketCard />
            <BasketCard />
            <BasketCard />
        </Popup>
    );
};

export default Basket;