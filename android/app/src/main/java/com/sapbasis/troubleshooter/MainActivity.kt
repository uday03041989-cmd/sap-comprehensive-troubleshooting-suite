package com.sapbasis.troubleshooter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ExperimentalMaterial3Api

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SAPBasisTroubleshooterApp()
        }
    }
}

@Composable
fun SAPBasisTroubleshooterApp() {
    val items = remember { troubleshootingCatalog() }
    var selected by remember { mutableStateOf(items.first()) }
    var issueText by remember { mutableStateOf(TextFieldValue("short dump in ABAP program")) }
    var result by remember {
        mutableStateOf(
            DiagnosticResult(
                title = "ABAP dump investigation",
                summary = "Inspect ST22, check user context, and validate RFC and locking issues.",
                actions = listOf(
                    "Run ST22 analysis",
                    "Check SU53 for missing authorization",
                    "Review SM12/SM13 lock entries"
                ),
                checks = listOf(
                    "Verify short dump details and exception type",
                    "Review job parameters and RFC destination",
                    "Check DB and ABAP performance counters"
                )
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        color = Color(0xFF0F172A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "SAP BASIS Mobile Troubleshooter",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Select a category",
                color = Color(0xFF93C5FD),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    val isSelected = item.id == selected.id
                    Button(
                        onClick = {
                            selected = item
                            issueText = TextFieldValue(item.sampleIssue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(item.title, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = selected.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = selected.summary,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    OutlinedTextField(
                        value = issueText,
                        onValueChange = { issueText = it },
                        label = { Text("Issue description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        singleLine = false,
                    )

                    Button(
                        onClick = {
                            result = diagnoseIssue(selected, issueText.text)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text("Run diagnosis")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = result.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = result.summary,
                        color = Color(0xFFCBD5E1),
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Recommended actions",
                        color = Color(0xFF7DD3FC),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    result.actions.forEach { action ->
                        Text("• $action", color = Color.White, modifier = Modifier.padding(top = 6.dp))
                    }

                    Text(
                        text = "Checks",
                        color = Color(0xFF7DD3FC),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    result.checks.forEach { check ->
                        Text("• $check", color = Color.White, modifier = Modifier.padding(top = 6.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SAPBasisTroubleshooterApp()
}
