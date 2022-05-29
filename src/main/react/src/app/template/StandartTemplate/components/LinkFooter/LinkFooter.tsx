import React from 'react';
import {ILinkFooter} from "./interfaces";
import './LinkFooter.scss'

const LinkFooter = ({icon = '', text = 'default'}: ILinkFooter) => {
    return (
        <a className='LinkFooter' href={icon}>
            <img className='icon' src={icon} alt="iconSocial" />
            <div className='text'>{ text }</div>
        </a>
    );
};

export default LinkFooter;