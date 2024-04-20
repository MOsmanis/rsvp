import React from 'react'
import ReactDOM from 'react-dom/client'
import {createBrowserRouter, RouterProvider, useParams} from "react-router-dom";
import App from './App.tsx'
import Guests from './Guests.tsx';
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css';

const router = createBrowserRouter([
  {
    path: "/:inviteCode",
    element: <App/>,
  },
  {
    path: "/",
    element: <App/>,
  },
  {
    path: "/guests",
    element: <Guests/>,
  },
]);


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
)
