import React from 'react';
import './ProductCard.scss';
import ButtonBuy from "../Buttons/ButtonBuy";
import productImg from "../../../../assets/radik.png";
import ButtonAddBasket from "../Buttons/ButtonAddBasket";

const ProductCard = () => {
    return (
        <div className="productCard">
            <img className="img" src={productImg} alt="product-img"/>
            <div className="info">
                <div className="productWrapper">
                    <div className='productText'>Радиатор</div>
                    <div className='productText'>350 RUB</div>
                </div>
                <div className='buttonsWrapper'>
                    <ButtonBuy />
                    <ButtonAddBasket />
                </div>
            </div>
        </div>
    );
};

export default ProductCard;