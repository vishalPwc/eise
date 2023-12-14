import React, { useState, useEffect } from 'react'
import './home.css'
// import Table from './Home/Table'
import Header from './Home/Header'
import Aside from './Home/Aside'
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import NativeSelect from '@material-ui/core/NativeSelect';
import { Search } from '@material-ui/icons';
import { Link, useRouteMatch, Route } from 'react-router-dom'

const useStyles = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
}));

export default function Home() {

    let match = useRouteMatch()

    console.log(match.url)

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


    const classes = useStyles();
    const [state, setState] = React.useState({
        age: '',
        name: 'hai',
    });

    const inputLabel = React.useRef(null);
    const [labelWidth, setLabelWidth] = React.useState(0);

    const handleChange = name => event => {
        setState({
            ...state,
            [name]: event.target.value,
        });
    };

    const handleSearch = (e) => {
        SetSearch(e.target.value)
    }


    return (
        <switch>
            <Header />
            <div className="main_content">
                <Aside />
                <div className="main_body">
                    <div>
                        <h1 style={{
                            color: "297AFF", borderBottom: "3px solid red", width: "314px"
                        }}><span style={{ fontSize: "40px", color: "red" }}>I</span>nvoice Summary</h1>
                    </div>
                    <hr style={{ top: "-17px", position: "relative" }} />
                    <div style={{ marginTop: "15px", position: "relative", display: "flex" }}>
                        <div>
                            <input type="text" placeholder="Search" style={{ marginLeft: "5px", padding: "7px", width: "100%" }} onChange={(e) => handleSearch(e)} />
                            <i className="fa fa-search" style={{
                                position: "absolute", left: "163px", top: "8px"
                            }}></i>
                        </div>
                        <div>
                            <FormControl className={classes.formControl} style={{ top: "-19px", left: "100px" }}>
                                <InputLabel htmlFor="age-native-simple">Age</InputLabel>
                                <Select
                                    native
                                    value={state.age}
                                    onChange={handleChange('age')}
                                    inputProps={{
                                        name: 'age',
                                        id: 'age-native-simple',
                                    }}
                                >
                                    <option value="" />
                                    <option value={10}>Ten</option>
                                    <option value={20}>Twenty</option>
                                    <option value={30}>Thirty</option>
                                </Select>
                            </FormControl>
                        </div>
                    </div>

                    <div>

                        {/* <Table data={response} search={search} /> */}

                    </div>

                </div>

            </div >
        </switch>
    )
}