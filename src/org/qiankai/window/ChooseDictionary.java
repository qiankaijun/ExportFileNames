package org.qiankai.window;

import org.qiankai.algorithm.AlphaNumComparator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChooseDictionary extends JFrame {
    private static final long serialVersionUID = 116835049430402866L;
    private String defaultPath;

    public static void main(String[] args) {
        new ChooseDictionary();
    }

    public ChooseDictionary() {
        String font = "微软雅黑";
        this.defaultPath = new File("").getAbsolutePath(); // 默认指向当前路径
        // 窗口----------------------------------------
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(ChooseDictionary.class.getResource("/org/qiankai/jpg/32.jpg"))); // 图标
        this.setTitle("文件列表导出"); // 标题
        this.setSize(400, 240); // 尺寸
        this.setLocationRelativeTo(null); // 定位再屏幕中央
        this.setVisible(true); // 窗口可见
        this.setResizable(false); // 不可改变窗口尺寸
        this.getContentPane().setLayout(null); // 设置无布局方式

        { // 标题----------------------------------------
            Label titleLabel = new Label("文件列表导出");
            titleLabel.setAlignment(Label.CENTER);
            titleLabel.setFont(new Font(font, Font.PLAIN, 28));
            titleLabel.setBounds(10, 10, 374, 50);
            this.getContentPane().add(titleLabel);
        }

        // 是否只显示文件夹----------------------------------------
        Checkbox folderOnlyCheckbox = new Checkbox("只显示文件夹");
        folderOnlyCheckbox.setBounds(86, 129, 100, 23);
        this.getContentPane().add(folderOnlyCheckbox);
        folderOnlyCheckbox.setFocusable(false);

        // 导出后是否打开文件夹----------------------------------------
        Checkbox openFolderCheckbox = new Checkbox("导出后打开文件夹");
        openFolderCheckbox.setBounds(210, 129, 115, 23);
        this.getContentPane().add(openFolderCheckbox);
        openFolderCheckbox.setFocusable(false);

        // 源文件夹部分----------------------------------------
        int sourceYyLocation = 70;
        int sourceHeight = 23;

        // 文本提示
        Label sourcePathLabel = new Label("源文件夹");
        sourcePathLabel.setFont(new Font(font, Font.PLAIN, 12));
        sourcePathLabel.setBounds(10, sourceYyLocation, 69, sourceHeight);
        this.getContentPane().add(sourcePathLabel);
        // 文件路径输入
        TextField sourcePathTextField = new TextField(this.defaultPath);
        sourcePathTextField.setBounds(86, sourceYyLocation, 240, sourceHeight);
        this.getContentPane().add(sourcePathTextField);
        // 选择路径按钮
        Button sourcePathButton = new Button("...");
        sourcePathButton.setBounds(340, sourceYyLocation, 40, sourceHeight);
        sourcePathButton.setFocusable(false);
        sourcePathButton.addActionListener(event->this.setPath(sourcePathTextField, sourcePathTextField.getText(), folderOnlyCheckbox.getState()));
        this.getContentPane().add(sourcePathButton);


        // 导出文件夹部分----------------------------------------
        int exportYLocation = 100;
        int exportHeight = 23;

        // 文本提示
        Label exportPathLabel = new Label("导出文件夹");
        exportPathLabel.setFont(new Font(font, Font.PLAIN, 12));
        exportPathLabel.setBounds(10, exportYLocation, 69, exportHeight);
        this.getContentPane().add(exportPathLabel);
        // 文件路径输入
        TextField exportPathTextField = new TextField(this.defaultPath);
        exportPathTextField.setBounds(86, exportYLocation, 240, exportHeight);
        this.getContentPane().add(exportPathTextField);
        // 选择路径按钮
        Button exportPathButton = new Button("...");
        exportPathButton.setBounds(340, exportYLocation, 40, exportHeight);
        exportPathButton.setFocusable(false);
        exportPathButton.addActionListener((event)->this.setPath(exportPathTextField, exportPathTextField.getText(), true));
        this.getContentPane().add(exportPathButton);


        // 按钮----------------------------------------
        int buttonYLocation = 168;
        int buttonHeight = 23;
        // 导出
        Button exportButton = new Button("导出");
        exportButton.setFont(new Font(font, Font.PLAIN, 12));
        exportButton.setBounds(75, buttonYLocation, 80, buttonHeight);
        exportButton.setFocusable(false);
        exportButton.addActionListener(event->{
            String sourcePath = sourcePathTextField.getText();
            File sourceFolder = new File(sourcePath);
            if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
                JOptionPane.showMessageDialog(this, "源文件夹有误，请检查源目录", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String exportPath = exportPathTextField.getText();
            File exportFolder = new File(exportPath);
            if (!exportFolder.isDirectory()) {
                int result = JOptionPane.showConfirmDialog(this, "导出路径错误，是否导出到本程序同目录？", "错误", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    exportPath = this.defaultPath;
                } else {
                    return;
                }
            }
            new AlphaNumComparator().getList(sourcePathTextField.getText(), exportPath);
            if (openFolderCheckbox.getState()) {
                try {
                    java.awt.Desktop.getDesktop().open(exportFolder);
                } catch (Exception e){}
            }
            JOptionPane.showMessageDialog(this, "导出成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        });
        this.getContentPane().add(exportButton);

        // 关闭
        Button closeButton = new Button("关闭");
        closeButton.setFont(new Font(font, Font.PLAIN, 12));
        closeButton.setBounds(236, buttonYLocation, 80, buttonHeight);
        closeButton.setFocusable(false);
        closeButton.addActionListener(event->System.exit(0));
        this.getContentPane().add(closeButton);
    }

    /**
     * 文件选择器
     * @param pathTextField // 文本输入框
     * @param defaultPath // 默认路径
     * @param dictionaryOnly // 是否仅显示文件夹
     */
    private void setPath(TextField pathTextField, String defaultPath, boolean dictionaryOnly) {
        JFileChooser jfc = new JFileChooser();
        File filePath = new File(defaultPath);
        jfc.setCurrentDirectory(filePath.exists() ? filePath : new File(this.defaultPath));
        jfc.setFileSelectionMode(dictionaryOnly ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showDialog(this, "选择");
        File file = jfc.getSelectedFile();
        if (file.isDirectory()) {
            pathTextField.setText(file.getAbsolutePath());
        } else {
            JOptionPane.showMessageDialog(this, "请选择文件夹！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
