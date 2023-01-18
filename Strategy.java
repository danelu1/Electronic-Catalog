import java.util.List;

interface Strategy {
	public Grade getBestScore(List<Grade> grades);
}