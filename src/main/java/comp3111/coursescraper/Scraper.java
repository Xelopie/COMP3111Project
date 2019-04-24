package comp3111.coursescraper;

import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomText;
import java.util.Vector;


/**
 * WebScraper provides a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br>
 * In this particular sample code, it access to HKUST class schedule and quota page (COMP). 
 * <br>
 * https://w5.ab.ust.hk/wcq/cgi-bin/1830/subject/COMP
 *  <br>
 * where 1830 means the third spring term of the academic year 2018-19 and COMP is the course code begins with COMP.
 * <br>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; div id="classes" &rarr; div class="course" &rarr;. You might see something like this:
 * <br>
 * <pre>
 * {@code
 * <div class="course">
 * <div class="courseanchor" style="position: relative; float: left; visibility: hidden; top: -164px;"><a name="COMP1001">&nbsp;</a></div>
 * <div class="courseinfo">
 * <div class="popup attrword"><span class="crseattrword">[3Y10]</span><div class="popupdetail">CC for 3Y 2010 &amp; 2011 cohorts</div></div><div class="popup attrword"><span class="crseattrword">[3Y12]</span><div class="popupdetail">CC for 3Y 2012 cohort</div></div><div class="popup attrword"><span class="crseattrword">[4Y]</span><div class="popupdetail">CC for 4Y 2012 and after</div></div><div class="popup attrword"><span class="crseattrword">[DELI]</span><div class="popupdetail">Mode of Delivery</div></div>	
 *    <div class="courseattr popup">
 * 	    <span style="font-size: 12px; color: #688; font-weight: bold;">COURSE INFO</span>
 * 	    <div class="popupdetail">
 * 	    <table width="400">
 *         <tbody>
 *             <tr><th>ATTRIBUTES</th><td>Common Core (S&amp;T) for 2010 &amp; 2011 3Y programs<br>Common Core (S&amp;T) for 2012 3Y programs<br>Common Core (S&amp;T) for 4Y programs<br>[BLD] Blended learning</td></tr><tr><th>EXCLUSION</th><td>ISOM 2010, any COMP courses of 2000-level or above</td></tr><tr><th>DESCRIPTION</th><td>This course is an introduction to computers and computing tools. It introduces the organization and basic working mechanism of a computer system, including the development of the trend of modern computer system. It covers the fundamentals of computer hardware design and software application development. The course emphasizes the application of the state-of-the-art software tools to solve problems and present solutions via a range of skills related to multimedia and internet computing tools such as internet, e-mail, WWW, webpage design, computer animation, spread sheet charts/figures, presentations with graphics and animations, etc. The course also covers business, accessibility, and relevant security issues in the use of computers and Internet.</td>
 *             </tr>	
 *          </tbody>
 *      </table>
 * 	    </div>
 *    </div>
 * </div>
 *  <h2>COMP 1001 - Exploring Multimedia and Internet Computing (3 units)</h2>
 *  <table class="sections" width="1012">
 *   <tbody>
 *    <tr>
 *        <th width="85">Section</th><th width="190" style="text-align: left">Date &amp; Time</th><th width="160" style="text-align: left">Room</th><th width="190" style="text-align: left">Instructor</th><th width="45">Quota</th><th width="45">Enrol</th><th width="45">Avail</th><th width="45">Wait</th><th width="81">Remarks</th>
 *    </tr>
 *    <tr class="newsect secteven">
 *        <td align="center">L1 (1765)</td>
 *        <td>We 02:00PM - 03:50PM</td><td>Rm 5620, Lift 31-32 (70)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td></tr><tr class="newsect sectodd">
 *        <td align="center">LA1 (1766)</td>
 *        <td>Tu 09:00AM - 10:50AM</td><td>Rm 4210, Lift 19 (67)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td>
 *    </tr>
 *   </tbody>
 *  </table>
 * </div>
 *}
 *</pre>
 * <br>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()).   
 * 
 *
 */
public class Scraper {
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public Scraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}
	
	private void addSection(HtmlElement e, Course c)
	{
		e = (HtmlElement)e.getFirstByXPath(".//td");
		String sect[] = e.asText().split(" ");	//The text before split looks like "L1 (1234)"
		Section s = new Section();
		s.setCode(sect[0]);
		s.setID(sect[1].substring(1, sect[1].length()-1));	//Take the id out of the brackets
		c.addSection(s);
	}
	
	private void addInstructor(HtmlElement e, Section sect)
	{
		List<?> instList = (List<?>)e.getByXPath(".//a");	//Instructor names are contained by all <a> elements that can be found
		if (instList == null)
			return;
		for (HtmlElement instElem: (List<HtmlElement>)instList)
		{
			Instructor inst = new Instructor();
			inst.setName(instElem.asText());
			sect.addInstructor(inst);
			
		}
	}
	
	private void addSlot(HtmlElement e, Section sect, boolean nonFirstRow)	//Boolean parameter originally named secondRow, changed to suit better for max number of slots being 3
	{
		String times[], venue;
		char probe = e.getChildNodes().get(nonFirstRow ? 0 : 3).asText().charAt(0);	//Take the first character of the time slot string
		//Check for exceptional case where the slots have date specified before slot, then the first 2 char will be integer[0..9]
		if ((int)probe >= 48 && (int)probe <= 57)
		{
			//Date and time are separated by <br> in html, which becomes "\n" by asText()
			times = e.getChildNodes().get(nonFirstRow ? 0 : 3).asText().split("\n");
			//After split("\n"), times[0] is the date, times[1] is the time we want to split again
			times = times[1].split(" ");
		}
		else
		{
			times = e.getChildNodes().get(nonFirstRow ? 0 : 3).asText().split(" ");
		}
		venue = e.getChildNodes().get(nonFirstRow ? 1 : 4).asText();
		if (times[0].equals("TBA"))
			return;
		for (int j = 0; j < times[0].length(); j+=2) {
			String code = times[0].substring(j , j + 2);
			if (Slot.DAYS_MAP.get(code) == null)
				break;
			Slot s = new Slot();
			s.setDay(Slot.DAYS_MAP.get(code));
			s.setStart(times[1]);
			s.setEnd(times[3]);
			s.setVenue(venue);
			sect.addSlot(s);	
		}

	}

	/**
	 * Returns a List containing Course scraped from an URL combined from the 3 parameters
	 * @param baseurl from the Base URL text field
	 * @param term from the Term text field
	 * @param sub from the Subject text field
	 * @return a List containing Course scraped from the combined URL
	 */
	public List<Course> scrape(String baseurl, String term, String sub) {

		try {
			
			HtmlPage page = client.getPage(baseurl + "/" + term + "/subject/" + sub);

			List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
			
			Vector<Course> result = new Vector<Course>();

			for (int i = 0; i < items.size(); i++) {
				Course c = new Course();
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				
				HtmlElement title = (HtmlElement) htmlItem.getFirstByXPath(".//h2");
				c.setTitle(title.asText());
				
				List<?> popupdetailslist = (List<?>) htmlItem.getByXPath(".//div[@class='popupdetail']/table/tbody/tr");
				HtmlElement exclusion = null;
				HtmlElement attribute = null;
				for ( HtmlElement e : (List<HtmlElement>)popupdetailslist) {
					HtmlElement t = (HtmlElement) e.getFirstByXPath(".//th");
					HtmlElement d = (HtmlElement) e.getFirstByXPath(".//td");
					if (t.asText().equals("EXCLUSION")) {
						exclusion = d;
					}
					if (t.asText().equals("ATTRIBUTES")) {
						attribute = d;
					}
				}
				c.setExclusion((exclusion == null ? "null" : exclusion.asText()));
				c.setAttribute((attribute == null ? "null" : attribute.asText()));

				List<?> htmlInfo = (List<?>)htmlItem.getByXPath(".//tr[contains(@class,'newsect')]");
				for (int j = 0; j < htmlInfo.size(); j++)

				{
					HtmlElement htmlElem = (HtmlElement)htmlInfo.get(j);
					
					addSection(htmlElem, c);
					addInstructor(htmlElem, c.getSection(j));
					addSlot(htmlElem, c.getSection(j), false);
					htmlElem = (HtmlElement)htmlElem.getNextSibling();
					if (htmlElem != null && !htmlElem.getAttribute("class").contains("newsect"))	//If the section has a second slot, and the second slot does not contain "newsect"
					{
						addSlot(htmlElem, c.getSection(j), true);
						htmlElem = (HtmlElement)htmlElem.getNextSibling();
						//If the section has a third slot. At this point the chance is that the section has more than 3 slots.
						//However, slot is defined to have a max size of 3
						if (htmlElem != null && !htmlElem.getAttribute("class").contains("newsect"))
						{
							addSlot(htmlElem, c.getSection(j), true);
						}
					}
				}

				result.add(c);
			}
			client.close();
			return result;
		} catch (Exception e) {
			//This should be the only error throwing operation that would appear on the system console
			//System.out.println(e);
		}
		return null; 
	}
	/**
	 * Returns a list containing subjects' name from an URL combined from 2 parameters
	 * @param baseurl from the Base URL text field
	 * @param term from the Term text field
	 * @return a List containing Subjects scraped from the combined URL
	 */
	public List<String> getSubjects(String baseurl, String term){
		try {
			HtmlPage page = client.getPage(baseurl + term + "/");
			List<?> items = (List<?>) page.getByXPath("//div[@class='depts']/a");
			Vector<String> result =  new Vector<String>();
			for(int i=0; i<items.size();++i) {
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				String subj = htmlItem.asText();
				result.add(subj);
			}
			client.close();
			return result;		
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	/**
	 * Still Working on, TBD
	 * @param sfqurl from SFQ url text field
	 * @param another param needed?(TBD)
	 * @return type TBD
	 */
	public List<String> getSFQData(String sfqurl, List<Section> courseList, List<Course> Courses){
		try {
			HtmlPage page = client.getPage(sfqurl);
			List<String> result = new Vector<String>();
			
			//List<?> Titles = (List<?>) page.getByXPath("//td[@colspan='3']");
			List<?> items = (List<?>) page.getByXPath("//td");
			
			for(int i=0;i<items.size();++i) {
				HtmlElement e = (HtmlElement) items.get(i);
				String title = e.asText();
				String titleCheck = title.replaceAll("\\s", "");
				for(Section s: courseList) {
					String enrollTitle = s.findCourseCode(Courses);
					/*works fine above*/
					if(titleCheck.equals(enrollTitle)) {
						result.add(titleCheck);				
						e = (HtmlElement) items.get(i+1);
						result.add(e.asText().substring(0,10));
					}
				}
			}
			
			
			return result;
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	/**
	 * Still Working on, TBD
	 * @param sfqurl from SFQ url text field
	 * @return type TBD
	 */
	public List<?> getInstructorSFQ(String sfqurl){
		try {
			HtmlPage page = client.getPage(sfqurl);
			/*still working*/
			List<?> temp = (List<?>) page.getByXPath("/html/body/table[@border=1]/tbody"); //get all table tag except first table
			
			//List<List<?>> temp2 = new Vector<List<?>>(); //create another list to store the list of data on each table
			
			for(int i=0; i<temp.size();++i) {
				HtmlElement htmlelement = (HtmlElement)temp.get(i);
				List<?> temp1 = (List<?>) htmlelement.getByXPath(".//tr");
				//  /html/body/table[3]/tbody/tr[4]/td[3]
			}
			return temp;
			/*still working*/
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
}