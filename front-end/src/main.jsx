import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './assets/css/main.css'
import './assets/css/style.css';
import './assets/css/colors/green.css';
import { CartProvider } from "./context/CartContext";
import ScriptInitializer from './components/ScriptInitializer';

ReactDOM.createRoot(document.getElementById('root')).render(
    <>
        <ScriptInitializer />
        <CartProvider>
        <App />
        </CartProvider>
    </>
)   