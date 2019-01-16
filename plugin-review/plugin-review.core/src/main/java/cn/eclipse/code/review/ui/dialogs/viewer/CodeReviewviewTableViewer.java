package cn.eclipse.code.review.ui.dialogs.viewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.eclipse.code.review.common.IDEUtils;
import cn.eclipse.code.review.model.QueryConditionModel;
import cn.eclipse.code.review.model.ReviewModel;
import cn.eclipse.code.review.services.DataServices;
import cn.eclipse.code.review.ui.dialogs.CodeReviewListDailog;

/**
 * @desciption code review list [todo list]
 * @author jack_fan
 * @date 2018年12月20日
 */
public class CodeReviewviewTableViewer {
	// table viewer
	private TableViewer tableViewer;
	private CodeReviewListDailog codeReviewListDailog;
	// 翻页页码
	public static int page = 1;
	public static int totalPage = 1;
	private QueryConditionModel condition;
	// setting data
	private DataServices services = new DataServices();
	/**
	 * @param codeReviewListDailog the codeReviewListDailog to set
	 */
	public void setCodeReviewListDailog(CodeReviewListDailog codeReviewListDailog) {
		this.codeReviewListDailog = codeReviewListDailog;
	}

	/**
	 * @desciption open the review list
	 * @author jack_fan
	 * @date 2018年12月20日
	 */
	public void init(Composite shell, QueryConditionModel condition) {
		page = 1;
		this.condition = condition;
		if (condition != null) {
			long count = services.count(condition);
			totalPage = (int) (count / condition.getPageSize() + (count % condition.getPageSize() != 0 ? 1 : 0));
		}
		// 初始化tableViewer
		createTableViewer(shell);
		if (tableViewer == null) {
			return;
		}
		tableViewer.setContentProvider(new TableViewerContentProvider());
		// setting column
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		// setting data
		if (!setData(condition)) {
			return;
		}
		addListener();
		ReviewActionGroup actionGroup = new ReviewActionGroup(tableViewer, codeReviewListDailog);
		actionGroup.fillContextMenu(new MenuManager());
	}

	/**
	 * @desciption 设置列表数据
	 * @author jack_fan
	 * @date 2018年12月21日
	 */
	public Boolean setData(QueryConditionModel condition) {
		if (services.count(condition) == 0) {
			MessageDialog.openInformation(null, "Error", "Code Review List is null!");
			return false;
		}
		tableViewer.setInput(services.getReviewRecordList(condition));
		codeReviewListDailog.setCurrentPageNumber(condition.getPage());
		return true;
	}

	/**
	 * @desciption 创建一个表格
	 * @author jack_fan
	 * @date 2018年12月20日
	 * @param prarent
	 */
	public void createTableViewer(Composite prarent) {
		tableViewer = new TableViewer(prarent,
				SWT.MULTI | SWT.H_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.FULL_SELECTION);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableLayout tLayout = new TableLayout();
		table.setLayout(tLayout);

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("project");

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("reviewer");

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("assigee");

		tLayout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("class");

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("line");

		tLayout.addColumnData(new ColumnWeightData(70));
		new TableColumn(table, SWT.NONE).setText("comment/issue/snippets");

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("status");

		tLayout.addColumnData(new ColumnWeightData(10));
		new TableColumn(table, SWT.NONE).setText("type");

		tLayout.addColumnData(new ColumnWeightData(30));
		new TableColumn(table, SWT.NONE).setText("time");
	}

	/**
	 * 新增加的监听器
	 */
	public void addListener() {
		tableViewer.getTable().addListener(SWT.MouseWheel, new Listener() {
		    @Override
		    public void handleEvent(Event e) {
		    	if(condition == null) {
		    		MessageDialog.openError(null, "Error", "condition is null!");
		    		return;
		    	}
				if (e.count < 0) {
					nextPage();
				} else if (e.count > 0) {
					prePage();
				}
		    }
		});
		
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {// IDoubleClickListener是一个接口
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				ReviewModel model = (ReviewModel) selection.getFirstElement();
				int line = model.getStartLine();
				if(line > 0) {
					line -=	1;
				}
				if(model.getClassPath() == null) {
					MessageDialog.openError(null, "Error", "File path is null!");
					return;
				}
				// 打开i指定文件、指定类
				if(model.getClassPath()!=null) {
					if (model.getProjectClassPath() != null && IDEUtils.open(model.getProjectClassPath(), line)) {
						codeReviewListDailog.close();
					}
				}	
			}
		});

		/*
		 * tv的选择事件（单击）监听
		 */
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
			}
		});
	}

	/**
	 * @desciption 上一页
	 * @author jack_fan
	 * @date 2019年1月11日
	 */
	public void nextPage() {
		if (totalPage <= page) {
			MessageDialog.openInformation(null, "Error", "This is last page!");
			return;
		}
		condition.setPage(++page);
		setData(condition);
	}

	/**
	 * @desciption 下一页
	 * @author jack_fan
	 * @date 2019年1月11日
	 */
	public void prePage() {
		if (1 >= page) {
			MessageDialog.openInformation(null, "Error", "This is first page!");
			return;
		}
		condition.setPage(--page);
		setData(condition);
	}
}