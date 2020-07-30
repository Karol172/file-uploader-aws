import React from 'react';
import Item from '../Item/Item';
import './ItemTable.css';

class ItemTable extends React.Component <any> {

    constructor(props :any) {
        super(props);
    }

    render () {
        return <div className="My-Table">
                <div className="Header-Row">
                    <div className="Header-Cell-First">No.</div>
                    <div className="Header-Cell-Name">Filename</div>
                    <div className="Header-Cell-Size">Size</div>
                    <div className="Header-Cell-Options">Options</div>
                </div>
            <div className="Content-Table">
                <Item id={1} filename={"example.txt"} size={128} />
                <Item id={2} filename={"example2.txt"} size={8} />
            </div>
            </div>;
    }
}

export default ItemTable;
