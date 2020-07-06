package kl.springboot.demo.utils;

import java.util.List;

public class Pagination<T> {
	private int total;
	private List<?> rows;

	public Pagination() {

	}

	public Pagination(int total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}
