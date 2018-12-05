package com.atc.common.utils;

import java.io.Serializable;
import java.util.Collection;

public class Paging<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -946271512316966821L;
	
	private int pageSize = 10;
	private Collection<T> data;
	private int totalCount;
	private int totalPage;
	private int startIndex = 0;
	private int currentPage = 1;
	
	public Paging(){
		
	}

	public Paging(Collection<T> data, int totalCount) {
		setTotalCount(totalCount);
		setData(data);
	}
	
	public Paging(int currentPage){
		setCurrentPage(currentPage);
	}

	public Collection<T> getData() {
		return this.data;
	}

	public void setData(Collection<T> data) {
		this.data = data;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return this.totalCount;
	}


	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
		} else
			this.totalCount = 0;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public void setStartIndex(int startIndex) {
		if (startIndex <= 0) {
			this.startIndex = 0;
		}else{
			this.startIndex = startIndex;
		}
	}

	public int getNextIndex() {
		int nextIndex = getStartIndex() + this.pageSize;

		if (nextIndex >= this.totalCount) {
			return getStartIndex();
		}

		return nextIndex;
	}

	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - this.pageSize;

		if (previousIndex <= 1) {
			return -1;
		}

		return previousIndex;
	}

	public int getLastPageIndex() {
		return (getTotalPage() - 1) * this.pageSize;
	}

	public int getCurrentPageIndex() {
		return getStartIndex() / this.pageSize + 1;
	}

	public int getTotalPage() {
		int totalPage = this.totalCount / this.pageSize;
		totalPage += (this.totalCount % this.pageSize == 0 ? 0 : 1);
		if(this.totalPage == 0){
			
		}
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
