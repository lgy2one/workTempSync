object Form1: TForm1
  Left = 153
  Top = 151
  Width = 1033
  Height = 759
  Align = alLeft
  AutoSize = True
  Caption = 'Form1'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 832
    Top = 32
    Width = 160
    Height = 40
    Caption = #28304#31243#24207#21517
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = #40657#20307
    Font.Style = []
    ParentFont = False
  end
  object Label2: TLabel
    Left = 832
    Top = 400
    Width = 193
    Height = 25
    AutoSize = False
    Caption = '08'#32423#35745#31185'(2)'#29677
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label3: TLabel
    Left = 840
    Top = 440
    Width = 153
    Height = 25
    AutoSize = False
    Caption = #23398#21495':3108006506'
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label4: TLabel
    Left = 840
    Top = 480
    Width = 153
    Height = 25
    AutoSize = False
    Caption = #22995#21517#65306#37073#26480#39213
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label5: TLabel
    Left = 840
    Top = 520
    Width = 161
    Height = 25
    AutoSize = False
    Caption = #24320#22987#35843#35797#26102#38388':'
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label6: TLabel
    Left = 840
    Top = 552
    Width = 145
    Height = 25
    AutoSize = False
    Caption = '2010-12-31'
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label7: TLabel
    Left = 840
    Top = 592
    Width = 153
    Height = 25
    AutoSize = False
    Caption = #32467#26463#35843#35797#26102#38388':'
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object Label8: TLabel
    Left = 840
    Top = 624
    Width = 153
    Height = 25
    AutoSize = False
    Caption = '2011-1-3'
    Color = clBtnFace
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -19
    Font.Name = 'MS Sans Serif'
    Font.Style = []
    ParentColor = False
    ParentFont = False
  end
  object ButtonRun: TButton
    Left = 836
    Top = 323
    Width = 139
    Height = 54
    Caption = 'RUN'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Times New Roman MT Extra Bold'
    Font.Style = [fsBold]
    ParentFont = False
    TabOrder = 0
    OnClick = ButtonRunClick
  end
  object Memo1: TMemo
    Left = 0
    Top = 16
    Width = 804
    Height = 729
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = #32043#20809#25340#38899#36755#20837#27861
    Lines.Strings = (
      '***** PL/0 Compiler Demo *****'
      '-'#20462#25913#20195#30721#30340#36807#31243#65292#23601#26159#25361#25112#30340#36807#31243'-')
    ParentFont = False
    ScrollBars = ssBoth
    TabOrder = 1
  end
  object EditName: TEdit
    Left = 831
    Top = 74
    Width = 162
    Height = 54
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
    Font.Name = 'Courier New'
    Font.Style = [fsBold]
    ImeName = #32043#20809#25340#38899#36755#20837#27861
    ParentFont = False
    TabOrder = 2
    Text = 'NEQ'
  end
  object ListSwitch: TRadioGroup
    Left = 832
    Top = 144
    Width = 177
    Height = 163
    Caption = #30446#26631#20195#30721
    Font.Charset = GB2312_CHARSET
    Font.Color = clWindowText
    Font.Height = -40
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
