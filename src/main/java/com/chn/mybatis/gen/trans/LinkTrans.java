package com.chn.mybatis.gen.trans;

import com.chn.mybatis.gen.def.LinkMetadata;

public class LinkTrans extends Trans {

	private ColumnTrans from;
	private ColumnTrans to;

	public LinkTrans(ColumnTrans from, ColumnTrans to) {
		this.from = from;
		this.to = to;
		if (this.from.getTableTrans() == this.to.getTableTrans()) {
			this.from.setTableTrans(TableTrans.forceNew(from.getTableName()));
			this.to.setTableTrans(TableTrans.forceNew(to.getTableName()));
		}
	}

	public LinkTrans(LinkMetadata link) {
		this(new ColumnTrans(link.getFrom()), new ColumnTrans(link.getTo()));
	}

	public ColumnTrans getFrom() {
		return from;
	}

	public ColumnTrans getTo() {
		return to;
	}

	public TableTrans getTargetTableTrans() {
		return to.getTableTrans();
	}

	public TableTrans getFromTableTrans() {
		return from.getTableTrans();
	}

	public String getFromColumnName() {
		return from.getName();
	}

	public String getToColumnName() {
		return to.getName();
	}
}
