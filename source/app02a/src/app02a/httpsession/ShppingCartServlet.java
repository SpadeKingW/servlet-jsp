package app02a.httpsession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="ShppingCartServlet",urlPatterns= {
		"/products","/viewProductDetails",
		"/addToCart","/viewCart"
})
public class ShppingCartServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2236220866079699849L;
	
	private static final String CART_ATTRIBUTE = "cart";
	private static final String CONTENT_TYPE = "text/html";
	
	private List<Product> products = new ArrayList<>();
	private NumberFormat currentFormat = NumberFormat.getCurrencyInstance(
			Locale.US);
	@Override
	public void init() throws ServletException {
		products.add(new Product(1, "Bravo 32' HDTV", 
				"Low-cost HDTV from renowened TV manufactuurer", 159.95f));
		products.add(
				new Product(2,"Bravo BluRay Player",
						"high quality stylish BluRay player", 99.95f));
		products.add(
				new Product(3, "Brav Stereoo System",
						"5 speaker hifi system with iPod play multiple formats", 39.5f));
		products.add(new Product(4, "Bravo iPod player",
				"An iPod plug-in that can play multiple fomats", 39.95f));
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (uri.endsWith("/products")) {
			sendProductList(response);
		}else if (uri.endsWith("/viewProductDetails")) {
			sendProductDetails(request, response);
		}else if (uri.endsWith("viewCart")) {
			showCart(request, response);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int productId = 0;
		int quantity = 0;
		try {
			productId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		
		Product product = getProduct(productId);
		if (product != null && quantity >= 0 ) {
			ShopopingItem shopopingItem = new ShopopingItem(product, quantity);
			HttpSession session = request.getSession();
			List<ShopopingItem> cart = (List<ShopopingItem>) session.
					getAttribute(CART_ATTRIBUTE);
			if (cart == null) {
				cart = new ArrayList<ShopopingItem>();
				session.setAttribute(CART_ATTRIBUTE, cart);
			}
			cart.add(shopopingItem);
			
			sendProductList(response);
		}
	}
	
 
	private void sendProductList(HttpServletResponse response) 
			throws IOException {
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Products<title>"
				+ "</head><body><h2>Products</h2>");
		writer.println("<ul>");
		for (Product product : products) {
			writer.println("<li>" + product.getName() +"("
					+currentFormat.format(product.getPrice())
					+")(" + "<a href='viewProductDetails?id="
					+ product.getId() +"'>Details</a>");
		}
		
		writer.println("</ul>");
		writer.println("<a href='viewCart'>View Cart</a>");
		writer.println("</body></html>");
	}
	
	private Product getProduct(int productId) {
		for (Product product : products) {
			if (product.getId() == productId) {
				return product;
			}
		}
		return null;
	}
	
	private void sendProductDetails(HttpServletRequest request
			,HttpServletResponse response) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter writer = response.getWriter();
		int productId = 0;
		try {
			productId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}
		
		Product product = getProduct(productId);
		
		if (product != null) {
			writer.println("<html><head>"
					+ "<title>Product Details</title></head>"
					+ "<body><h2>Prduct Details</h2>"
					+ "<form method='post' action='addToCart'>");
			writer.println("<input type='hidden' name='id'"
					+ "value='"
					+ productId
					+"'/>");
			writer.println("<table>");
			writer.println("<tr><td>Name:</td></td>"
					+ product.getName()+"</td><tr>");
			writer.println("<tr><td>Description:</td><td>"
					+ product.getDescription()+"</td></tr>");
			writer.println("<tr>"
					+ "<tr>"
					+ "<td><input name='quantity'/></td>"
					+ "<td><input type='submit' value='Buy'/>"
					+ "</td>"
					+ "</tr>");
			writer.println("<tr><td colspan='2'>"
					+ "<a href='prducts'>Product List</a>"
					+ "</td></tr>");
			writer.println("</table>");
			writer.println("</form></body>");
		}else {
			writer.println(" No product found ");
		}
		
	}
	
	private void showCart(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter writer = response.getWriter();
		writer.println("<html><head><title>Shopping Cart</head></title>"
				+ "</head>");
		writer.println("<body><a href='products'>"
				+ "Product List</a>");
		HttpSession session = request.getSession();
		List<ShopopingItem> cart = (List<ShopopingItem>) session.getAttribute(CART_ATTRIBUTE);
		if (cart != null) {
			writer.println("<table>");
			writer.println("<tr><td style='width:150px'>Quantity"
					+ "</td>"
					+ "<td style='width:150px'>Product</td>"
					+ "<td style='width:150px'>Price</td>"
					+ "<td>Amount</td></tr>");
			double total = 0.0;
			for (ShopopingItem shopopingItem : cart) {
				Product product = shopopingItem.getProduct();
				int quantity = shopopingItem.getQuantity();
				if (quantity != 0) {
					float price = product.getPrice();
					writer.println("<tr>");
					writer.println("<td>" + quantity + "</td>");
					writer.println("<td>" + product.getName() + "</td>");
					writer.println("<td>" + currentFormat.format(price) + "</td>");
					double subtotle = price * quantity;
					writer.println("<td>" + currentFormat.format(subtotle)+"</td>");
					total += subtotle;
					writer.println("</tr>");
				}
			}
			
			writer.println("<tr><td  colspan='4'"
					+ "style='text-align:right'>"
					+ "Total:"
					+ currentFormat.format(total)
					+ "</td></tr>");
			writer.println("</table>");
		}
		writer.println("</table></body></html>");
	}
	
	
	
	
}
