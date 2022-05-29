import React from 'react';
// @ts-ignore
import logo from '../../../../../assets/steam-logo.png';

import './ButtonAuth.scss'

const ButtonAuth = () => {
    return (
        <button className='buttonAuth'>
            <img className='logoSteam' src={logo} alt="logo-ateam"/>
            <div className='buttonAuth'>login steam</div>
        </button>
    );
};

export default ButtonAuth;