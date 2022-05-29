import React, {FC, PropsWithChildren} from 'react';
import './ButtonIcon.scss'
import {IButtonIcon} from "./interfaces";

const ButtonIcon: FC<PropsWithChildren<IButtonIcon>> = ({children,onClick, borderNone = true}) => {

    return (
        <button className={`buttonIcon ${borderNone ? 'borderNone' : ''}`} onClick={onClick}>
            {children}
        </button>
    );
};

export default ButtonIcon;