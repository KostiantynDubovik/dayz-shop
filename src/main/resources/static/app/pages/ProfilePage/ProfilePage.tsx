import React, {ChangeEvent, useState} from 'react';
import './ProfilePage.scss'
import ButtonBuy from "../../template/StandartTemplate/components/Buttons/ButtonBuy";
import {Route} from "react-router-dom";

const ProfilePage = () => {
    const [stateInputBalance, setStateInputBalance] = useState('');
    const [stateInputCode, setStateInputCode] = useState('');
    const onChangeBalance = (e: ChangeEvent<HTMLInputElement>) => {
        const { target: { value }} = e
        setStateInputBalance(value)
    }
    const onChangeCode = (e: ChangeEvent<HTMLInputElement>) => {
        const { target: { value }} = e
        setStateInputCode(value)
    }

    return (
        <div className='container profile'>
            <div className='paperBlock'>
                <div className='balanceAdd'>
                    <h3 className='profileTitle'>Пополнение баланса</h3>
                    <div className='wrapperProfile'>
                        <div className='d-flex'>
                            <select className='select' name="Выберете способ" id="">
                                <option value="" selected disabled hidden>Выберете способ</option>
                                <option value="visa">Visa/Mastercard</option>
                                <option value="yoomoney">YooMoney</option>
                                <option value="qiwi">Qiwi</option>
                                <option value="paypal">PayPal</option>
                                <option defaultValue={"Выберете"} >PayPal</option>
                            </select>
                            <div className='d-flex align-center money'>
                                <input value={stateInputBalance} onChange={onChangeBalance} placeholder={'100'} className='inputMoney' type="number" />
                                <div className='currency'>
                                    RUB
                                </div>
                            </div>
                        </div>
                    </div>
                    <ButtonBuy border text={'Пополнить'} />
                </div>
            </div>
            <div className='paperBlock'>
                <div className='wrapperProfile'>
                    <h3 className='profileTitle'>Ввести промо-код</h3>
                    <input value={stateInputCode} onChange={onChangeCode} placeholder={'Вставьте промо-код'} className='inputCode' type="text" />
                </div>
                <ButtonBuy border text={'Активировать'} />
            </div>
            <div className='paperBlock widthFull'>
                <h3 className='profileTitle'>История платежей</h3>

            </div>
        </div>

    );
};

export default ProfilePage;