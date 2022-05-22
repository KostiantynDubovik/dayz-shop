import React from 'react';
import './Footer.scss';
import LinkFooter from "../LinkFooter";
import discordLogo from '../../../../assets/discord-logo.png';
import VkLogo from '../../../../assets/vk-logo.png';

const Footer = () => {
    return (
        <div className='footer'>
            <div className="containerFooter">
                <LinkFooter icon={discordLogo} text={"Discord"} />
                <LinkFooter icon={VkLogo} text={"Вконтакте"} />
                <div style={{display: 'block', marginBottom: 'auto'}}>
                    <a style={{
                        color: '#fff', fontWeight: 'bold',
                        fontSize: '12px', textDecoration: "none",
                    }} href="#">
                        разработчики данного магазина
                    </a>
                </div>
            </div>
        </div>
    );
};

export default Footer;