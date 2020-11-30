package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.wicketstuff.egrid.provider.EditableListDataProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 * @param <T>
 */
public class EditableGridFormComponentWrapper<T extends Serializable> extends FormComponentPanel<List<T>> {

    private Class<T> clazz;
    private long pageSize;
    private List<IColumn<T, String>> columns = new ArrayList<>();
    private List<T> intialList;

    public EditableGridFormComponentWrapper(String id, IModel<List<T>> model) {
        super(id, model);
        setOutputMarkupId(true);
        setRenderBodyOnly(true);
    }

    @Override
    protected void onInitialize() {
        intialList = getModelObject();
        createEditableGrid();
        super.onInitialize();
    }

    @Override
    public void convertInput() {
        setConvertedInput(intialList);
    }

    protected void createEditableGrid() {
        DgEditableGrid<T, String> editableGrid = new DgEditableGrid<T, String>("grid", columns,
                new EditableListDataProvider<T, String>(new LoadableDetachableModel<List<T>>() {
                    @Override
                    protected List<T> load() {
                        return getModelObject();
                    }
                }.getObject()) {
                    @Override
                    public IModel<T> model(T object) {
                        return new CompoundPropertyModel<>(super.model(object));
                    }
                }, pageSize, clazz) {

            @Override
            protected void onError(AjaxRequestTarget target) {
                EditableGridFormComponentWrapper.this.onError(target);
                super.onError(target);
            }

            @Override
            protected void onAdd(AjaxRequestTarget target, T newRow) {
                EditableGridFormComponentWrapper.this.onAdd(target, newRow);
                super.onAdd(target, newRow);
            }

            @Override
            protected void onSave(AjaxRequestTarget target, IModel<T> rowModel) {
                EditableGridFormComponentWrapper.this.onSave(target);
                super.onSave(target, rowModel);
            }

            @Override
            protected void onCancel(AjaxRequestTarget target) {
                EditableGridFormComponentWrapper.this.onCancel(target);
                super.onCancel(target);
            }

            @Override
            protected void onDelete(AjaxRequestTarget target, IModel<T> rowModel) {
                EditableGridFormComponentWrapper.this.onDelete(target);
                super.onDelete(target, rowModel);
            }
        };
        editableGrid.setTableCss("table table-hover");
        add(editableGrid);
    }

    public void onAdd(AjaxRequestTarget target, T newRow) {

    }

    public void onCancel(AjaxRequestTarget target) {

    }

    public void onDelete(AjaxRequestTarget target) {

    }

    public void onSave(AjaxRequestTarget target) {

    }

    public void onError(AjaxRequestTarget target) {

    }

    public List<IColumn<T, String>> getColumns() {
        return columns;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
