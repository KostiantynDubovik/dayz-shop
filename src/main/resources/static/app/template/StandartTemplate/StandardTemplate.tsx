import React, {useState} from 'react';
import Navbar from "./components/Navbar/Navbar";
import ProductCard from "./components/ProductCard";

import './StandardTemplate.scss'
import Footer from "./components/Footer";
import Basket from "./components/Basket";

const StandardTemplate = () => {
    const [activePopup, setActivePopup] = useState<boolean>(false)
    return (
        <>
            <div className='container'>
                <div className="wrapperCard">
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                    <ProductCard />
                </div>
                <Basket activePopup={activePopup} setActivePopup={setActivePopup} />
            </div>
            <Footer />
        </>
    );
};

export default StandardTemplate;