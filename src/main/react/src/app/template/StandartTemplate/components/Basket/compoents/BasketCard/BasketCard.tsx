import React, {useState} from 'react';
import './BasketCard.scss';
import imgCard from '../../../../../../assets/radik.png';
import ButtonIcon from "../../../Buttons/ButtonIcon";
import MinusIcon from '../../../../../../assets/minusIcon.png';
import PlusIcon from '../../../../../../assets/plusIcon.png';


const BasketCard = () => {
    const [state, setState]= useState(1)
    const decrement = () => setState(state > 1 ? state - 1 : state)
    const increment = () => setState(state + 1)
    return (
        <div className='BasketCard'>
            <img className='imgCard' src={imgCard} alt="img"/>
            <div className='space-between'>
                <div>
                    <h4>Радиатор</h4>
                    <div className='d-flex'>
                        <ButtonIcon onClick={decrement} borderNone={false}>
                            <img style={{height: '10px', width: '10px'}} src={MinusIcon} alt=""/>
                        </ButtonIcon>
                        <div className='displayCount'> {state} </div>
                        <ButtonIcon onClick={increment} borderNone={false}>
                            <img style={{height: '10px', width: '10px'}} src={PlusIcon} alt=""/>
                        </ButtonIcon>
                    </div>
                </div>
                <div className='priceProduct'>
                    670 RUB
                </div>
            </div>
        </div>
    );
};

export default BasketCard;