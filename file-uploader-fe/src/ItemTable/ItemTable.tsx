import React from 'react';
import Item from '../Item/Item';
import './ItemTable.css';

class ItemTable extends React.Component <any> {

    constructor(props :any) {
        super(props);
    }

    render () {
        const items = this.props.items.map((item:any, key:number) =>
            <Item id={(this.props.page-1)*this.props.size + key+1} filename={item.filename} size={item.size} />
        )

        return <div className="My-Table">
                <div className="Header-Row">
                    <div className="Header-Cell-First">No.</div>
                    <div className="Header-Cell-Name">Filename</div>
                    <div className="Header-Cell-Size">Size</div>
                    <div className="Header-Cell-Options">Options</div>
                </div>
            <div className="Content-Table">
                {items}
            </div>
            </div>;
    }
}

export default ItemTable;
