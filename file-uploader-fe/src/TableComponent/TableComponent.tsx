import React from 'react';
import DataTable from "react-data-table-component";

const columns = [
    {
        name: 'No',
        selector: 'no',
        center: true,
        id: 0,
    },
    {
        name: 'Filename',
        selector: 'filename',
        sortable: true,
        id: 1,
    },
    {
        name: 'Size',
        selector: 'size',
        center: true,
        id: 2,
    },
    {
        name: 'Options',
        selector: 'options',
        center: true,
        id: 3,
    },
];

interface IProps {
}

interface ITableComponentState {
    rows: any,
    pageNumber: number,
    pageSize: number,
    searchPhrase: string,
    sort: boolean,
    sortDir?: ('asc' | 'desc'),
    totalRecords: 0,
}

class TableComponent extends React.Component<IProps, ITableComponentState> {

    constructor(props :IProps) {
        super(props);
        this.state = {
            rows: [],
            pageNumber: 0,
            pageSize: 20,
            searchPhrase: "",
            sort: false,
            totalRecords: 0,
        };
    };

    componentDidMount() {
        this.fetchItems();
    }

    fetchItems = (pageNumber :number = this.state.pageNumber, pageSize :number = this.state.pageSize,
                  searchPhrase:string = this.state.searchPhrase, sort: boolean = this.state.sort,
                  dir: ('asc' | 'desc' | undefined) = this.state.sortDir) => {
        const url = process.env.REACT_APP_API_URL + "/api/file?phrase="+searchPhrase+"&page="+pageNumber+ "&size="+pageSize +
            (sort ? "&sort=filename," + dir : "");
        fetch(url)
            .then(res => res.json())
            .then(result => {
                const startIndex = result.size*result.number;
                return {data: result.content.map((item:any, index:number) => {
                        return {
                            no: startIndex + index + 1,
                            filename: item.filename,
                            size: this.formatSize(item.size),
                            options: "Usuń",
                        }
                }),
                    total: result.totalElements,
                };
            })
            .then(result => this.setState({
                rows: result.data,
                totalRecords: result.total,
                pageNumber: pageNumber,
                pageSize: pageSize,
                searchPhrase: searchPhrase,
                sort: sort,
                sortDir: dir,
            }))
            .catch((error) => {
                console.log(error);
            })
    };

    onChangePage = (page :number, totalRows: number) => {
        this.fetchItems(page-1);
    }

    onChangeRowsPerPage = (currentRowsPerPage :number, currentPage :number) => {
        let page :number = Math.floor(this.state.pageSize / currentRowsPerPage * (currentPage - 1));
        this.fetchItems(page, currentRowsPerPage);
    }

    onSort = (dir: ('asc' | 'desc')) => {
        this.fetchItems(0, this.state.pageSize, this.state.searchPhrase, true, dir);
    }

    formatSize = (size :number) => {
        const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
        let units_index = 0;
        while (size > 1024) {
            size /= 1024;
            units_index++;
        }
        return size.toFixed(1) + ' ' + units[units_index];
    };

    render = () => <div className={"tableComponent"}>
                        <DataTable
                            columns={columns}
                            data={this.state.rows}
                            pagination={true}
                            paginationServer={true}
                            paginationRowsPerPageOptions={[5, 10, 20, 50, 100]}
                            paginationPerPage={this.state.pageSize}
                            paginationTotalRows={this.state.totalRecords}
                            onChangePage={((page, totalRows) => {this.onChangePage(page, totalRows)})}
                            onChangeRowsPerPage={(currentRowsPerPage, currentPage) => {
                                this.onChangeRowsPerPage(currentRowsPerPage, currentPage)}
                            }
                            sortServer={true}
                            onSort={((column, sortDirection) => {
                                this.onSort(sortDirection)})}
                        />
                    </div>;

}

export default TableComponent;
