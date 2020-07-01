object Form1: TForm1
  Left = -8
  Top = -8
  Width = 1384
  Height = 898
  Align = alLeft
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 11
  object Label1: TLabel
    Left = 704
    Top = 27
    Width = 136
    Height = 33
    Caption = #28304#31243#24207#21517
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = #40657#20307
    Font.Style = []
    ParentFont = False
  end
  object Label1l: TLabel
    Left = 704
    Top = 400
    Width = 272
    Height = 33
    Caption = #23398#21495#65306'3117004652'
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = #40657#20307
    Font.Style = []
    ParentFont = False
  end
  object Label1y: TLabel
    Left = 704
    Top = 440
    Width = 493
    Height = 33
    Caption = #29677#32423#65306'17'#32423#35745#31639#26426#31185#23398#19982#25216#26415'6'#29677
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = #40657#20307
    Font.Style = []
    ParentFont = False
  end
  object Label1x: TLabel
    Left = 704
    Top = 480
    Width = 204
    Height = 33
    Caption = #22995#21517#65306#37101#23478#35946
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = #40657#20307
    Font.Style = []
    ParentFont = False
  end
  object ButtonRun: TButton
    Left = 714
    Top = 334
    Width = 118
    Height = 46
    Caption = 'RUN'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = 'Times New Roman MT Extra Bold'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 0
    OnClick = ButtonRunClick
  end
  object Memo1: TMemo
    Left = 7
    Top = 7
    Width = 666
    Height = 834
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = #32043#20809#25340#38899#36755#20837#27861
    Lines.Strings = (
      '***** PL/0 Compiler Demo *****')
    ParentFont = False
    ScrollBars = ssBoth
    TabOrder = 1
  end
  object EditName: TEdit
    Left = 703
    Top = 63
    Width = 137
    Height = 45
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = #32043#20809#25340#38899#36755#20837#27861
    ParentFont = False
    TabOrder = 2
    Text = 'test'
  end
  object ListSwitch: TRadioGroup
    Left = 697
    Top = 142
    Width = 150
    Height = 138
    Caption = #30446#26631#20195#30721
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -33
    Font.Name = #40657#20307
    Font.Style = []
    ItemIndex = 0
    Items.Strings = (
      #26174#31034
      #19981#26174#31034)
    ParentFont = False
    TabOrder = 3
  end
end
