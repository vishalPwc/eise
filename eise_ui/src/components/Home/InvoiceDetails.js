import React, { useState, useEffect } from 'react'
import '../home.css'
// import Table from './Table'
import Header from './Header'
import Aside from './Aside'
// import DetailTable from './DetailTable'
import { Link, useRouteMatch, Route } from 'react-router-dom'

export default function InvoiceDetails() {
    let match = useRouteMatch()

    const [response, setResponse] = useState('');
    const [search, SetSearch] = useState('');



    useEffect(
        () => {
            loadJ();
        }, [])


    async function loadJ() {
        try {
            let res = await fetch('/A.json');
            let user = await res.json();
            console.log(user)
            setResponse(user)

        }
        catch (err) {
            console.log(err);
        }
    }

    const handleSearch = (e) => {
        SetSearch(e.target.value)
    }
    return (
        <>
            <Header />
            <div className="main_content">
                <Aside />
                <div className="main_body">
                    <div>
                        <h1 style={{
                            color: "297AFF", borderBottom: "3px solid red", width: "314px"
                        }}><span style={{ fontSize: "40px", color: "red" }}>I</span>nvoice Details</h1>
                    </div>
                    <hr style={{ top: "-17px", position: "relative" }} />
                    <div style={{ marginTop: "15px", position: "relative", display: "flex" }}>
                        <div>
                            <input type="text" placeholder="Search" style={{ marginLeft: "5px", padding: "7px", width: "100%" }} onChange={(e) => handleSearch(e)} />
                            <i className="fa fa-search" style={{
                                position: "absolute", left: "163px", top: "8px"
                            }}></i>
                        </div>
                    </div>

                    <div>
                        <div style={{ marginTop: "5px", marginLeft: "5px" }}>
                            <span style={{ fontWeight: "600" }}>Order</span> : 40000695  | <span style={{ fontWeight: "600" }}>Invoice</span> : Settlement
                    </div>

                    </div>

                    <div style={{ display: "flex" }}>
                        <div>
                            {/* <DetailTable className="invoiceSummary" data={response} search={search} /> */}
                        </div>
                    </div>

                </div>

            </div >
        </>
    )
}