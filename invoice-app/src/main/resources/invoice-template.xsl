<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:functx="http://www.functx.com">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="content"
                                       page-width="210mm" page-height="297mm"
                                       margin-top="10mm" margin-bottom="10mm"
                                       margin-left="10mm" margin-right="10mm">

                    <fo:region-body />
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="content">
                <fo:flow flow-name="xsl-region-body">
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="70mm" />
                        <fo:table-column column-width="50mm" />
                        <fo:table-column column-width="70mm" />
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell column-number="1">
                                    <fo:block><fo:external-graphic src="src/main/resources/increff-logo.png" content-width="15mm"/></fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="2">
                                    <fo:block font-size="16pt" font-family="sans-serif" font-weight="bold">Positron Inc.</fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="3">
                                    <fo:block
                                            font-family="sans-serif" font-size="24pt"
                                            color="#BBBBBB" text-align="right"
                                            >INVOICE</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell column-number="1">
                                    <fo:block font-family="sans-serif" font-size="10pt">The Hub Unit 1,</fo:block>
                                    <fo:block font-family="sans-serif" font-size="10pt">Sarjapur - Marathahalli Rd, Bellandur</fo:block>
                                    <fo:block font-family="sans-serif" font-size="10pt">Bengaluru, Karnataka 560103</fo:block>
                                </fo:table-cell>
                                <fo:table-cell column-number="3" padding-top="10pt">
                                    <fo:table table-layout="fixed" width="100%">
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block font-family="sans-serif" font-size="10pt">Invoice date-time:</fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block font-family="sans-serif" font-size="10pt"><xsl:value-of select="invoiceForm/timestamp"/></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block font-family="sans-serif" font-size="10pt">Invoice number:</fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block font-family="sans-serif" font-size="10pt"><xsl:value-of select="invoiceForm/invoiceNumber"/></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell column-number="3" padding-top="7mm">
                                    <fo:block font-size="10pt" font-family="sans-serif">Bill to:</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                            <fo:table-row>
                                <fo:table-cell column-number="3">
                                    <xsl:call-template name="billTo" />
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>

                    <fo:block space-before="10pt">
                        <fo:table border="1pt solid #888">
                            <fo:table-column column-width="15mm" />
                            <fo:table-column column-width="40mm" />
                            <fo:table-column column-width="30mm" />
                            <fo:table-column column-width="35mm" />
                            <fo:table-column column-width="35mm" />
                            <fo:table-column column-width="35mm" />
                            <fo:table-body>
                                <fo:table-row font-size="12pt" background-color="#EEE" border="1pt solid #888">
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Serial no.</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm">
                                        <fo:block>Description</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm" text-align="right">
                                        <fo:block>Quantity</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm" text-align="right">
                                        <fo:block>Selling Price</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm" text-align="right">
                                        <fo:block>Mrp</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="1mm 2mm" text-align="right">
                                        <fo:block>Total</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <xsl:if test="count(invoiceForm/invoiceItemList/itemNumber) > 0">
                                    <xsl:for-each select="invoiceForm/invoiceItemList">
                                        <xsl:variable name="background">
                                            <xsl:choose>
                                                <xsl:when test="position() mod 2 = 0">
                                                    <xsl:text>#EEE</xsl:text>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:text>#FFF</xsl:text>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:variable>

                                        <fo:table-row font-size="8pt" background-color="{$background}">
                                            <fo:table-cell padding="1mm 2mm">
                                                <fo:block><xsl:value-of select="itemNumber"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="1mm 2mm">
                                                <fo:block white-space="pre"><xsl:value-of select="name"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="1mm 2mm" text-align="right">
                                                <fo:block><xsl:value-of select="format-number(quantity, '0.0')"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="1mm 2mm" text-align="right">
                                                <fo:block><xsl:value-of select="format-number(sellingPrice, '0.00')"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="1mm 2mm" text-align="right">
                                                <fo:block><xsl:value-of select="format-number(mrp, '0.00')"/></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="1mm 2mm" text-align="right">
                                                <fo:block><xsl:value-of select="format-number(quantity * sellingPrice, '0.00')"/></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </xsl:if>

                            </fo:table-body>
                        </fo:table>
                    </fo:block>

                    <fo:block space-before="20pt" page-break-inside="avoid">
                        <fo:table>
                            <fo:table-column column-width="120mm" />
                            <fo:table-column column-width="10mm" />
                            <fo:table-column column-width="45mm" />
                            <fo:table-body>
                                <fo:table-row font-size="6pt" font-weight="bold">
                                    <fo:table-cell padding="0mm 2mm">
                                        <fo:block>Notes</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding="0mm 2mm" column-number="3">
                                        <fo:block>Sub total</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>

                                <xsl:apply-templates select="invoiceForm/invoiceItemList[1]"/>

                            </fo:table-body>
                        </fo:table>
                    </fo:block>


                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>


    <xsl:template match="invoiceItemList">
        <xsl:param name="runningTotal" select="number('0')"/>
        <xsl:choose>
            <xsl:when test="following-sibling::invoiceItemList[1]">
                <xsl:apply-templates select="following-sibling::invoiceItemList[1]">
                    <xsl:with-param name="runningTotal" select="$runningTotal + quantity * sellingPrice"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>

                <xsl:variable name="subtotal" select="$runningTotal + quantity * sellingPrice"/>
                <xsl:variable name="cgst" select="$subtotal * 0.12"/>
                <xsl:variable name="total" select="$subtotal * 1.12"/>

                <fo:table-row font-size="8pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell padding="1mm 2mm" border="1pt solid #888" number-rows-spanned="6">
                        <fo:block><xsl:value-of select="/root/notes"/></fo:block>
                    </fo:table-cell>
                    <fo:table-cell font-size="12pt" padding="1mm 2mm" column-number="3" border="1pt solid #888" text-align="right">
                        <fo:block>
                            <xsl:choose>
                                <xsl:when test="itemNumber">
                                    <xsl:value-of select="format-number($subtotal, '0.00')"/> Rs
                                </xsl:when>
                                <xsl:otherwise>
                                    0.00 Rs
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-size="8pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell padding="2mm 1mm 0mm 2mm" column-number="3" font-size="6pt" font-weight="bold">
                        <fo:block>cgst</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-size="12pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell padding="1mm 2mm" column-number="3" border="1pt solid #888" text-align="right">
                        <fo:block>
                            <xsl:choose>
                                <xsl:when test="itemNumber">
                                    <xsl:value-of select="format-number($cgst, '0.00')"/> Rs
                                </xsl:when>
                                <xsl:otherwise>
                                    0.00 Rs
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-size="8pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell padding="2mm 1mm 0mm 2mm" column-number="3" font-size="6pt" font-weight="bold">
                        <fo:block>Total price</fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-size="12pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell padding="1mm 2mm" column-number="3" border="1pt solid #888" text-align="right">
                        <fo:block>
                            <xsl:choose>
                                <xsl:when test="itemNumber">
                                    <xsl:value-of select="format-number($total, '0.00')"/> Rs
                                </xsl:when>
                                <xsl:otherwise>
                                    0.00 Rs
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row xmlns:fo="http://www.w3.org/1999/XSL/Format">
                    <fo:table-cell column-number="3">
                        <fo:block></fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="billTo">
        <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:block color="#666666" font-size="10pt" font-family="sans-serif">
                <xsl:value-of select="invoiceForm/customerName"/>
                <xsl:value-of select="' '"/>
                <xsl:value-of select="invoiceForm/email"/>
                <xsl:value-of select="' '"/>
                <xsl:value-of select="invoiceForm/phone"/>
                <xsl:value-of select="' '"/>

            </fo:block>
        </fo:block>
    </xsl:template>
</xsl:stylesheet>